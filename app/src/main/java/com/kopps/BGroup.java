package com.kopps;

import java.util.ArrayList;

// 비콘 그룹 클래스 - 동적 배열로 그룹 관리
// 비콘 id1=uuid, id2=major, id3=minor 로 비콘 구별
// b_name 으로 비콘에게 별명 부여
class BGroup {
    String bg_name;
    ArrayList<String[]> bg_array; // 비콘식별 배열 로 이루어진 비콘 그룹 리스트

    // 생성자
    BGroup(String name){
        this.bg_name = name;
        this.bg_array = new ArrayList<String[]>();
    }
    String get_BGname() {
        return this.bg_name;
    }
    // 비콘을 그룹에 추가
    void add_beacon(String id1, String id2, String id3, String b_name){
        // 리스트에 추가할 모습으로 형변환
        String[] temp_b = {id1,id2,id3,b_name};
        // 리스트에 추가
        this.bg_array.add(temp_b);
    }
    // 비콘을 그룹에서 삭제
    void del_beacon(int index){
        this.bg_array.remove(index);
    }
}
