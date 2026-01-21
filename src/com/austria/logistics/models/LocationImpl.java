package com.austria.logistics.models;

import com.austria.logistics.models.enums.Cities;
import com.austria.logistics.models.contracts.Location;
import java.time.LocalDateTime;

public class Location implements Location {
    private Cities location;
    private LocalDateTime eventTime;
}
