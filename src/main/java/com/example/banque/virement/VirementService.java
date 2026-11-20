package com.example.banque.virement;

import com.example.banque.compte.Compte;
import com.example.banque.compte.CompteRepository;
import com.example.banque.utilisateur.out.UtilisateurCourant;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class VirementService {

    private static final int TAILLE_PAGE = 20;

    private final VirementRepository virements;
    private final CompteRepository comptes;
    private final UtilisateurCourant utilisateurCourant;

    public VirementService(VirementRepository virements, CompteRepository comptes, UtilisateurCourant utilisateurCourant) {
        this.virements = virements;
        this.comptes = comptes;
        this.utilisateurCourant = utilisateurCourant;
    }

    @Transactional(readOnly = true)
    public List<VirementDto> lister(Long compteId, int page) {
        var pageable = PageRequest.of(page, TAILLE_PAGE, Sort.by(Sort.Direction.DESC, "id"));
        return virements.findByCompteId(compteId, pageable).stream().map(VirementDto::de).toList();
    }

    @PreAuthorize("hasRole('CLIENT')")
    public VirementDto creer(NouveauVirement demande) {
        Compte compte = comptes.findById(demande.compteId())
                .orElseThrow(() -> new NoSuchElementException("Compte " + demande.compteId() + " introuvable"));
        var virement = new Virement(compte, demande.ibanDestinataire(), demande.montant());
        return VirementDto.de(virements.save(virement));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public VirementDto valider(Long id) {
        Virement virement = virements.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Virement " + id + " introuvable"));
        String login = utilisateurCourant.get()
                .orElseThrow(() -> new IllegalStateException("Personne n'est connecté"))
                .getLogin();
        virement.valider(login);
        return VirementDto.de(virement);
    }
}
