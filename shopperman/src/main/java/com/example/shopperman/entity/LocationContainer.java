package com.example.shopperman.entity;

import lombok.Data;

@Data
public class LocationContainer {
	private Location location;
	private RequesterLocation requesterLocation;
	private MarketLocation marketLocation;
}
