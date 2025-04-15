package com.evo.notification_fcm.application.mapper;

import com.evo.common.dto.event.PushNotificationEvent;
import com.evo.notification_fcm.application.dto.request.RegisterOrUpdateDeviceRequest;
import com.evo.notification_fcm.domain.command.RegisterOrUpdateDeviceCmd;
import com.evo.notification_fcm.domain.command.StoredNotificationCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandMapper {
    StoredNotificationCmd from(PushNotificationEvent pushNotificationEvent);

    RegisterOrUpdateDeviceCmd from(RegisterOrUpdateDeviceRequest registerOrUpdateDeviceRequest);
}
