//package com.example.grammar.chomsky;
//
//import com.example.grammar.Production;
//import lombok.Getter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ChomskyProduction implements Production {
//    /**
//     * 左边
//     */
//    private String from;
//
//    /**
//     * 右边
//     */
//    @Getter
//    private String derive1;
//
//    /**
//     * 右边
//     */
//    @Getter
//    private String derive2;
//
//    @Override
//    public String leftSymbol() {
//        return from;
//    }
//
//    @Override
//    public List<String> rightSymbol() {
//        return new ArrayList<>(derive2 == null ? List.of(derive1) : List.of(derive1, derive2));
//    }
//
//
//
//}
