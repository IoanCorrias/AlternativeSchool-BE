package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.DeliveryFolder;
import com.federicoioan.alternativeschool.model.dto.DeliveryFolderDto;

import java.util.List;

public interface DeliveryFolderService {

    DeliveryFolder createDeliveryFolder(Long courseId, DeliveryFolderDto deliveryFolder);

    List<DeliveryFolder> findDeliveryFolders(Long courseId);

    DeliveryFolder findDeliveryFolder(Long id);

    DeliveryFolder updateDeliveryFolder(Long id, DeliveryFolderDto deliveryFolder);

    DeliveryFolder deleteDeliveryFolder(Long id);
}
