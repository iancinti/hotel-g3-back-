package com.g3.hotel_g3_back.service.appliction.port.out;

import com.g3.hotel_g3_back.service.domain.Service;

public interface CreateServiceRepository {
    void execute(Service service);
}
