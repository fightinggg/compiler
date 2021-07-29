package com.example.grammar.augment.lr;

import com.example.grammar.Production;
import com.example.utils.TableUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Getter
public class LRTable {

    @Getter
    @AllArgsConstructor
    public static class Action {
        public static String ACC = "acc";

        private final String ac;
        private final Integer jump;

        @Override
        public String toString() {
            return ac.equals(ACC) ? ACC : ac + jump;
        }
    }

    private final Integer[][] gotoTable;
    private final Action[][] actionTable;
    private final Production[] productionArray;
    private final String[] symbolList;
    private final Integer begin;

    private final Map<Production, Integer> productionId;
    private final Map<String, Integer> symbolId;


    public LRTable(Integer[][] gotoTable, Action[][] actionTable, Production[] productionArray,
                   String[] symbolList, Integer begin) {
        this.gotoTable = gotoTable;
        this.actionTable = actionTable;
        this.productionArray = productionArray;
        this.symbolList = symbolList;
        this.begin = begin;
        this.productionId = IntStream.range(0, productionArray.length).boxed().collect(Collectors.toMap(i -> productionArray[i], i -> i));
        this.symbolId = IntStream.range(0, symbolList.length).boxed().collect(Collectors.toMap(i -> symbolList[i], i -> i));
    }


    @Override
    public String toString() {
        List<List<String>> productionHead = IntStream.range(0, productionArray.length)
                .mapToObj(i -> List.of(String.valueOf(i), productionArray[i].toString()))
                .collect(Collectors.toList());

        List<List<String>> table = IntStream.range(0, gotoTable.length)
                .boxed()
                .map(i -> Stream.concat(Arrays.stream(gotoTable[i]).map(Objects::toString), Arrays.stream(actionTable[i]).map(Objects::toString)))
                .map(Stream::toList)
                .toList();

        return TableUtils.tableToString(productionHead) + TableUtils.tableToString(table);

    }
}
