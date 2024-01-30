package com.example.shopperman.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.MarketLocation;
import com.example.shopperman.entity.RequesterLocation;

public class TspGreedyAlgorithm {

	private List<Location> locationList; // 정렬되지 않은 Location 리스트
	private int numOfLocations; // Location의 수
	private boolean[] visited; // 방문한 Location인지 여부를 저장하는 배열
	private Double[][] distances; // Location 간 거리를 저장하는 배열
	private ArrayList<Integer> minPath; // 최소 경로 (인덱스 번호)
	private ArrayList<Integer> idCheckList; // id 체크 리스트

	// 심부름 요청자 위치, 가게 위치, 경유지 위치 모두 담은 locationList를 입력 (심부름 제공자 현재 위치 제외)
	public TspGreedyAlgorithm(List<Location> locationList) {
		this.locationList = locationList;
		this.numOfLocations = locationList.size() + 1;
		this.visited = new boolean[locationList.size() + 1];
		this.distances = new Double[locationList.size() + 1][locationList.size() + 1];
		this.minPath = new ArrayList<Integer>();
		this.idCheckList = new ArrayList<Integer>();
	}

	// Location 최단 경로 리스트를 반환해주는 메소드
	public ArrayList<Location> getTspOrderedLocationList(String currentMapX, String currentMapY) {

		// 각각의 Location 사이 거리 Matrix 작성
		for (int i = 0; i < numOfLocations; i++) {
			for (int j = 0; j < numOfLocations; j++) {

				// 비교대상1
				BigDecimal aMapX;
				BigDecimal aMapY;
				if (i == 0) {
					aMapX = new BigDecimal(currentMapX);
					aMapY = new BigDecimal(currentMapY);
				} else {
					aMapX = new BigDecimal(locationList.get(i - 1).getMapX());
					aMapY = new BigDecimal(locationList.get(i - 1).getMapY());
				}

				// 비교대상2
				BigDecimal bMapX;
				BigDecimal bMapY;

				if (j == 0) {
					bMapX = new BigDecimal(currentMapX);
					bMapY = new BigDecimal(currentMapY);
				} else {
					bMapX = new BigDecimal(locationList.get(j - 1).getMapX());
					bMapY = new BigDecimal(locationList.get(j - 1).getMapY());
				}

				BigDecimal x = aMapX.subtract(bMapX);
				BigDecimal y = aMapY.subtract(bMapY);

				BigDecimal x2 = x.multiply(x);
				BigDecimal y2 = y.multiply(y);

				BigDecimal distance = x2.add(y2);

				// x2 + y2 의 값을 입력
				distances[i][j] = distance.doubleValue();
			}
		}

		// Greedy 알고리즘 실행
		ArrayList<Integer> pathOrderList = getGreedyPathOrder();

		// Location 순서 변경
		ArrayList<Location> orderedLocationList = new ArrayList<>();
		for (int i = 1; i < pathOrderList.size(); i++) {
			orderedLocationList.add(locationList.get(pathOrderList.get(i) - 1));
		}
		
		//--------------------------------TEST--------------------------------
		System.out.println();
		System.out.println();
		System.out.println("This is Greedy Algorithm");
		System.out.println();
		
		// 원래 Location 순서대로 addr 출력
		System.out.printf("원래 순서:   ");
		for(Location location : locationList) {
			System.out.printf("[%s]  ", location.getRoadName());
		}
		System.out.println();
		System.out.println();
		// 정렬된 Location 순서대로 addr 출력
		System.out.printf("정렬된 순서:  ");
		for(Location location : orderedLocationList) {
			System.out.printf("[%s]  ", location.getRoadName());
		}
		System.out.println();
		System.out.println();
		System.out.println("Location 총 "+(numOfLocations-1)+"개");
		System.out.println();
		System.out.println();
		//--------------------------------TEST--------------------------------

		return orderedLocationList;
	}

	// Greedy 알고리즘 실행해서, Location 리스트 인덱스 번호들의 순서를 반환하는 메소드 (0부터 시작)
	private ArrayList<Integer> getGreedyPathOrder() {
		// 시작 지역 등록 & 방문 체크
		minPath.add(0);
		visited[0] = true;

		// i번째 index에 들어갈 Location 찾기 (i => minPath의 다음 인덱스 번호)
		for (int i = 1; i < numOfLocations; i++) {
			int closestLocation = -1;
			Double closestDistance = Double.MAX_VALUE;

			// 방문하지 않은 Location 중 가장 가까운 Location 찾아서 저장 (j => i를 정하기 위해서 iterate 되는 숫자)
			for (int j = 1; j < numOfLocations; j++) {
				// 방문하지 않았고, 현재 가장 가까운 Location면 저장
				if (!visited[j] && distances[minPath.get(i - 1)][j] < closestDistance) {
					// 다음 Location이 RequesterLocation 인 경우 (심부름 요청자 리스트에 존재하는지 체크)
					if(locationList.get(j - 1) instanceof RequesterLocation) {
						RequesterLocation r = (RequesterLocation) locationList.get(j - 1);
						// 심부름 요청자 리스트에 이름이 존재하면
						if(idCheckList.contains(r.getId())) {
							// 현재까지 가장 가까운 Location으로 등록
							closestLocation = j;
							closestDistance = distances[minPath.get(i - 1)][j];
						}
					}
					// 다음 Location이 RequesterLocation 아닌 경우
					else {
						// 현재까지 가장 가까운 Location으로 등록
						closestLocation = j;
						closestDistance = distances[minPath.get(i - 1)][j];
					}
				}
			}
			
			// 다음 Location이 MarketLocation 이면, 심부름 요청자 리스트 적어두기
			if(locationList.get(closestLocation - 1) instanceof MarketLocation) {
				MarketLocation m = (MarketLocation) locationList.get(closestLocation - 1);
				if(m.getId() != null) {
					idCheckList.add(m.getId());
				}
			}
			// 다음 Location이 RequesterLocation 이면, 심부름 요청자 리스트 다시 지우기 (메모리를 위해서)
			else if(locationList.get(closestLocation - 1) instanceof RequesterLocation) {
				RequesterLocation r = (RequesterLocation) locationList.get(closestLocation - 1);
				if(r.getId() != null) {
					idCheckList.remove(r.getId());
				}
			}
			
			// 현재 가장 가까운 Location 등록 & 방문 체크
			minPath.add(closestLocation);
			visited[closestLocation] = true;
		}

		return minPath;
	}
}
