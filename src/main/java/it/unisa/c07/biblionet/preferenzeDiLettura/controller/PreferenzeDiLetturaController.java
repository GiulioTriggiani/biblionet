package it.unisa.c07.biblionet.preferenzeDiLettura.controller;

import com.sun.istack.Nullable;
import it.unisa.c07.biblionet.model.entity.Genere;
import it.unisa.c07.biblionet.model.entity.utente.Esperto;
import it.unisa.c07.biblionet.model.entity.utente.HaGenere;
import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import it.unisa.c07.biblionet.preferenzeDiLettura.service.PreferenzeDiLetturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Alessio Casolaro
 * @author Antonio Della Porta
 */
@Controller
@RequiredArgsConstructor
@SessionAttributes("loggedUser")
@RequestMapping("/preferenze-di-lettura")
public class PreferenzeDiLetturaController {

    /**
     * Il service per effettuare le operazioni di persistenza.
     */
    private final PreferenzeDiLetturaService preferenzeDiLetturaService;

    /**
     * Implementa la funzionalità di controllare se l'utente in sessione
     * è abilitato ad inserire dei generi, se lo è riceve tutti i generi
     * presenti nel database e rimanda l'utente alla pagina di
     * inserimento dei generi, altrimenti lo rimanda alla home.
     *
     * @param model utilizzato per ottenere l'utente in sessione
     *
     * @return la view di inserimento dei generi se l'utente è
     * Esperto o Lettore, la home altrimenti,
     */
    @RequestMapping("/generi")
    public String generiLetterari(final Model model){

            UtenteRegistrato utenteRegistrato= (UtenteRegistrato) model.getAttribute("loggedUser");

            if(utenteRegistrato!= null && (utenteRegistrato.getTipo().equals("Esperto") || utenteRegistrato.getTipo().equals("Lettore"))) {

                HaGenere utente=(HaGenere) utenteRegistrato;
                List<Genere> allGeneri = preferenzeDiLetturaService.getAllGeneri();
                List<Genere> generiUtente = utente.getGeneri();

                for(Genere genere : generiUtente){
                    allGeneri.remove(genere);
                }

                model.addAttribute("generiUtente",generiUtente);
                model.addAttribute("generi",allGeneri);
                return "preferenze-lettura/modifica-generi";
            }
            else {
                return "index";
            }
    }

    /**
     * Implementa la funzionalità di inserire o rimuovere generi ad un esperto
     * oppure ad un lettore.
     * @param generi i generi che il lettore o l'esperto dovranno possedere
     * @param model utilizzato per prendere l'utente loggato a cui modificare
     *              i generi
     * @return la pagina home
     */
    @RequestMapping(value = "/modifica-generi",method = RequestMethod.POST)
    public String test(@RequestParam("genere") String[]generi, final Model model){

        List<Genere> toAdd= preferenzeDiLetturaService.getGeneriByName(generi);
        UtenteRegistrato utenteRegistrato= (UtenteRegistrato) model.getAttribute("loggedUser");

        if(utenteRegistrato!=null && utenteRegistrato.getTipo().equals("Esperto")) {

            preferenzeDiLetturaService.addGeneriEsperto(toAdd,(Esperto) utenteRegistrato);

        }
        return "index";
    }





}
