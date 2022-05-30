package com.garena.dnfmaster.service;

import com.garena.dnfmaster.repo.CharacRepo;
import com.garena.dnfmaster.repo.PvpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacService {
    @Autowired
    private CharacRepo characRepo;
    @Autowired
    private PvpRepo pvpRepo;
}
