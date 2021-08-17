package com.example.pava.impl;

import com.example.pava.PavaCode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PavaCodeImpl implements PavaCode<List<PavaDefaultThreeAddressCode>> {
    private final List<PavaDefaultThreeAddressCode> codes;

    public PavaCodeImpl(String codes) {
        List<PavaDefaultThreeAddressCode> pavaDefaultThreeAddressCodes = Arrays.stream(codes.split("\n"))
                .map(PavaDefaultThreeAddressCode::decode).toList();
        this.codes = pavaDefaultThreeAddressCodes.stream().toList();
    }

    @Override
    public String type() {
        return "PavaDefaultThreeAddressCode";
    }

    @Override
    public String version() {
        return "v1.0.0";
    }

    @Override
    public List<PavaDefaultThreeAddressCode> pavaCode() {
        return codes;
    }
}
