package com.grd.gradingbe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/admin")
public class AdminController
{
    @GetMapping("/system32")
    public void testRole ()
    {
    }
}
