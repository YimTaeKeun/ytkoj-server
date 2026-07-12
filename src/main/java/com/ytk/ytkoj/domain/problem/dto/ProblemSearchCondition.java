package com.ytk.ytkoj.domain.problem.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/***
 * 문제 조건 검색에 사용됩니다.
 */
@Builder
@Getter
public class ProblemSearchCondition {

    private final String searchText;

    private final List<String> searchTagNames;

    private final List<SortType> sortTypes;


    public boolean hasSearchText(){
        // null이 아니고 비어있지 않은가
        return (searchText != null) && (!searchText.isBlank());
    }

    public boolean hasSortTypes(){
        return (sortTypes != null) && (!sortTypes.isEmpty());
    }

    public boolean hasSearchTags(){
        return (searchTagNames != null) && (!searchTagNames.isEmpty());
    }


    /***
     *  DIFFICULTY_DESC // 난이도 내림차순
     *  </br>
     *  DIFFICULTY_ASC // 난이도 오름차순
     *  </br>
     *  PNUM_ASC // 문제 번호 오름차순
     *  </br>
     *  PNUM_DESC // 문제 번호 내림차순
     *  </br>
     *  PNAME_ASC // 문제 이름 오름차순
     *  </br>
     *  PNAME_DESC // 문제 이름 내림차순
     */
    public enum SortType{
        DIFFICULTY_DESC,
        DIFFICULTY_ASC,
        PNUM_ASC,
        PNUM_DESC,
        PNAME_ASC,
        PNAME_DESC
    }
}
