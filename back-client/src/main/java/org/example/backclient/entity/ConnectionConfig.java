package org.example.backclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionConfig {
    String address;
    String token;
    String networkInterface;
}
