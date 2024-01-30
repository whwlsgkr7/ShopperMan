package com.example.shopperman.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.shopperman.entity.Location;
import com.example.shopperman.entity.MarketLocation;
import com.example.shopperman.entity.RequesterLocation;

public class TspBackTrackingAlgorithm {

	private List<Location> locationList; // 정렬되지 않은 Location 리스트
    private int numOfLocations; // Location의 수
    private boolean[] visited; // 방문한 Location인지 여부를 저장하는 배열
    private Double[][] distances; // Location 간 거리를 저장하는 배열
    private Double minDistance; // 최소 거리
    private ArrayList<Integer> minPath; // 최소 경로 (인덱스 번호)
    private ArrayList<Integer> idCheckList; // id 체크 리스트
    
    public TspBackTrackingAlgorithm(List<Location> locationList) {
    	this.locationList = locationList;
        this.numOfLocations = locationList.size() + 1;
        this.visited = new boolean[locationList.size() + 1];
        this.distances = new Double[locationList.size() + 1][locationList.size() + 1];
        this.minDistance = Double.MAX_VALUE;
        this.minPath = new ArrayList<Integer>();
        this.idCheckList = new ArrayList<Integer>();
    }
    
    // Location 최단 경로 리스트를 반환해주는 메소드
	public ArrayList<Location> getTspOrderedLocationList(String currentMapX, String currentMapY) {
		
		// 각각의 Location 사이 거리 Matrix 작성
		for (int i = 0; i < locationList.size() + 1; i++) {
			for (int j = 0; j < locationList.size() + 1; j++) {

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
		
		// TSP BackTracking 알고리즘 실행
		ArrayList<Integer> pathOrderList = getPathOrder();
		
		// Location 순서 변경
		ArrayList<Location> orderedLocationList = new ArrayList<>();
		for (int i = 1; i < pathOrderList.size(); i++) {
			orderedLocationList.add(locationList.get(pathOrderList.get(i) - 1));
		}
		
		//--------------------------------TEST--------------------------------
		System.out.println();
		System.out.println();
		System.out.println("This is BackTracking Algorithm");
		System.out.println();
		
		// 원래 Location 순서대로 addr 출력
		System.out.printf("원래 순서:   ");
		for(Location p : locationList) {
			System.out.printf("[%s]  ", p.getRoadName());
		}
		System.out.println();
		System.out.println();
		// 정렬된 Location 순서대로 addr 출력
		System.out.printf("정렬된 순서:  ");
		for(Location p : orderedLocationList) {
			System.out.printf("[%s]  ", p.getRoadName());
		}
		System.out.println();
		System.out.println();
		System.out.println("Location 총 "+(numOfLocations-1)+"개");
		System.out.println();
		System.out.println();
		//--------------------------------TEST--------------------------------
		
		return orderedLocationList;
	}
    
	
    // BackTracking 알고리즘 실행해서, 경로 순서를 반환하는 메소드
    private ArrayList<Integer> getPathOrder() {
    	
        ArrayList<Integer> path = new ArrayList<>(); // 경로 순서
        
        // 시작 Location 설정
        int startLocation = 0; // 시작 Location을 0으로 설정
        int currentLocation = startLocation; // 시작 Location을 현재 Location으로 설정
        Double totalDistance = (double) 0; // 총 거리 초기화
        path.add(startLocation); // 경로에 시작 Location 삽입
        visited[startLocation] = true; // 시작 Location은 방문한 것으로 설정
        
        // 재귀함수 호출
        findTspPath(path, currentLocation, totalDistance, 1);
        
        // 최소 경로 반환
        return minPath;
    }
    
    // 재귀함수를 이용하여 최단 경로를 찾는 메소드
    private void findTspPath(ArrayList<Integer> path, int currentLocation, Double totalDistance, int count) {
    	
        // 모든 Location를 방문했을 경우, 다시 시작 Location으로 돌아가기
        if (count == numOfLocations) {
            // 마지막 Location과 시작 Location을 연결한 거리를 더하여 총 거리 계산
            totalDistance += distances[currentLocation][0];
            
            // 현재까지의 경로와 거리가 최소값보다 작을 경우 "거리" & "최소 경로" 갱신
            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                minPath = new ArrayList<>(path);
            }
            return;
        }
        
        // 아직 모든 Location을 전부 방문한게 아닌 경우 (i => 총 Location 리스트 인덱스 번호 (현재 Location = 0))
        for (int i = 1; i < numOfLocations; i++) {
            if (!visited[i]) { // 방문하지 않은 Location인 경우
            	// 다음 Location이 RequesterLocation 인 경우 (심부름 요청자 리스트에 존재하는지 체크)
            	if(locationList.get(i - 1) instanceof RequesterLocation) {
            		RequesterLocation r = (RequesterLocation) locationList.get(i - 1);
					// 심부름 요청자 리스트에 이름이 존재하면 방문 처리
            		if(idCheckList.contains(r.getId())) {
						visited[i] = true; // 방문 처리
		                path.add(i); // 경로에 추가
		                totalDistance += distances[currentLocation][i]; // 거리 더하기
		                findTspPath(path, i, totalDistance, count + 1); // 재귀함수 호출
		                visited[i] = false; // 방문 처리 취소
		                path.remove(path.size() - 1); // 경로에서 제거
		                totalDistance -= distances[currentLocation][i]; // 거리 빼기
					}
            	}
            	// 다음 Location이 RequesterLocation 이 아닌 경우
            	else {
            		// 다음 Location이 MarketLocation 인 경우, 심부름 요청자 리스트 적어두기
        			if(locationList.get(i - 1) instanceof MarketLocation) {
        				MarketLocation m = (MarketLocation) locationList.get(i - 1);
        				if(m.getId() != null) {
        					idCheckList.add(m.getId());
        				}
        			}
            		visited[i] = true; // 방문 처리
                    path.add(i); // 경로에 추가
                    totalDistance += distances[currentLocation][i]; // 거리 더하기
                    findTspPath(path, i, totalDistance, count + 1); // 재귀함수 호출
                    // 다음 Location이 MarketLocation 인 경우, 심부름 요청자 리스트 다시 지우기
                    if(locationList.get(i - 1) instanceof MarketLocation) {
                    	MarketLocation m = (MarketLocation) locationList.get(i - 1);
                    	if(m.getId() != null) {
                    		idCheckList.remove(m.getId());
                    	}
                    }
                    visited[i] = false; // 방문 처리 취소
                    path.remove(path.size() - 1); // 경로에서 제거
                    totalDistance -= distances[currentLocation][i]; // 거리 빼기
            	}
            }
        }
    }
}