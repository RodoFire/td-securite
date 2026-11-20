package com.example.banque.compte.in;

import com.example.banque.compte.CompteDto;
import com.example.banque.compte.CompteService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
public class CompteController {

    private final CompteService comptes;

    public CompteController(CompteService comptes) {
        this.comptes = comptes;
    }

    @GetMapping
    public List<CompteDto> lister() {
        return comptes.lister();
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.id == authentication.name")
    public CompteDto trouver(@PathVariable Long id) {
        return comptes.trouver(id);
    }
}
