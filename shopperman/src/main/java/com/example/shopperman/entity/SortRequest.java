package com.example.shopperman.entity;

import java.util.List;

import lombok.Data;

@Data
public class SortRequest {
	private Location myLocation;
	private List<Integer> postIdList;
}
