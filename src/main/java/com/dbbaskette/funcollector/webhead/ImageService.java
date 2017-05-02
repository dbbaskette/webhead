package com.dbbaskette.funcollector.webhead;
import javaxt.io.Image;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by dbaskette on 3/22/17.
 */

@Service
public class ImageService {

    private static String UPLOAD_ROOT = "upload-dir";

    private final ImageRepository repository;
    private final ResourceLoader resourceLoader;
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public ImageService(ImageRepository repository, ResourceLoader resourceLoader,RabbitTemplate rabbitTemplate,SimpMessagingTemplate messagingTemplate) {

        this.repository = repository;
        this.resourceLoader = resourceLoader;
        this.rabbitTemplate = rabbitTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    public Resource findOneImage(String filename) {
        return resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename);
    }

    public void createImage(MultipartFile file) throws IOException {

        if (!file.isEmpty()) {

            Image image = new Image(file.getInputStream());
            image.rotate();
            image.saveAs( UPLOAD_ROOT + "/" + file.getOriginalFilename());
            UploadedImage uploadedImage =new UploadedImage(file.getOriginalFilename());
            repository.save(uploadedImage);
            uploadedImage.setImage(file.getBytes());
            rabbitTemplate.convertAndSend("vision-ready",uploadedImage);
            //messagingTemplate.convertAndSend("/topic/newImage", file.getOriginalFilename());
        }

    }

    public void changeImageInformation(Long id,String description,double value) throws IOException {

        UploadedImage uploadedImage = repository.findById(id);
        deleteImage(uploadedImage.getName(),false);
        uploadedImage.setDescription(description);
        uploadedImage.setValue(value);
        repository.save(uploadedImage);

    }



    public void deleteImage(String filename,boolean deletFile) throws IOException {

        final UploadedImage byName = repository.findByName(filename);
        repository.delete(byName);
        if (deletFile) {
            Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
        }
    }

    @RabbitListener(queues = "identified")
    public void receiveMessage(UploadedImage uploadedImage) {
        //uploadedImage.setImage(temp);
        //repository.save(uploadedImage);
        try {
            changeImageInformation(uploadedImage.getId(),uploadedImage.getDescription(),uploadedImage.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("RECEIVED: "+uploadedImage.getDescription());
        messagingTemplate.convertAndSend("/topic/idImage", uploadedImage.getId()+","+uploadedImage.getDescription());

    }


    public Page<UploadedImage> findPage(Pageable pageable){
        return repository.findAll(pageable);
    }



    /**
     * Pre-load some fake images
     *
     * @return Spring Boot {@link CommandLineRunner} automatically run after app context is loaded.
     */
    @Bean
    CommandLineRunner setUp(ImageRepository repository) throws IOException {

        return (args) -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

            Files.createDirectory(Paths.get(UPLOAD_ROOT));


        };

    }

}
