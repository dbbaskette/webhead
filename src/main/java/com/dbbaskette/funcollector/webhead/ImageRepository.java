package com.dbbaskette.funcollector.webhead;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by dbaskette on 3/22/17.
 */


public interface ImageRepository extends PagingAndSortingRepository<UploadedImage,Long>{

    public UploadedImage findByName(String name);

    public UploadedImage findById(Long id);
}
