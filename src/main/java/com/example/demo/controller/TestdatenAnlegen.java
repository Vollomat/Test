package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RequestMapping("/testdaten")
@RestController
public class TestdatenAnlegen {

    private VorstellungRepository vorstellungRepository;
    private SitzplaetzeRepository sitzplaetzeRepository;
    private SitzplaetzeFuerVorstellungRepository sitzplaetzeFuerVorstellungRepository;
    private KinoRepository kinoRepository;
    private KinoSaalRepository kinoSaalRepository;
    private OMDBRepository omdbRepository;

    public TestdatenAnlegen(VorstellungRepository vorstellungRepository, SitzplaetzeRepository sitzplaetzeRepository, SitzplaetzeFuerVorstellungRepository sitzplaetzeFuerVorstellungRepository, KinoRepository kinoRepository, KinoSaalRepository kinoSaalRepository, OMDBRepository omdbRepository) {
        this.vorstellungRepository = vorstellungRepository;
        this.sitzplaetzeRepository = sitzplaetzeRepository;
        this.sitzplaetzeFuerVorstellungRepository = sitzplaetzeFuerVorstellungRepository;
        this.kinoRepository = kinoRepository;
        this.kinoSaalRepository = kinoSaalRepository;
        this.omdbRepository = omdbRepository;
    }

    @RequestMapping(value ="/kinosaalplaetze", produces = "application/json", method = RequestMethod.POST)
    @PostMapping(value ="/kinosaalplaetze", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void testdatenKinosaalplaetzeAnlegen() {
        for (int anzahlKinoSaal = 4; anzahlKinoSaal > 0; anzahlKinoSaal--) {
            for (int i = 1; i <= 5; i++) {
                for (int j = 1; j <= 8; j++) {
                    Sitzplaetze sitzplaetz = new Sitzplaetze();
                    sitzplaetz.setReihe(i);
                    sitzplaetz.setSpalte(j);
                    sitzplaetz.setKinosaalID(anzahlKinoSaal);
                    sitzplaetzeRepository.save(sitzplaetz);
                }
            }
        }
        System.out.println("Kinosaal-Plätze eingefügt!");
    }

        @RequestMapping(value ="/vorstellungen", produces = "application/json", method = RequestMethod.POST)
        @PostMapping(value ="/vorstellungen", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public void testdatenVorstellungenAnlegen() {
            VorstellungController vorstellungController = new VorstellungController(vorstellungRepository, sitzplaetzeRepository, sitzplaetzeFuerVorstellungRepository);
            for(int j = 0; j < omdbRepository.findAll().size(); j++) {
                for (int i = 1; i < 5; i++) {
                    Vorstellungen vorstellung = new Vorstellungen();
                    if(i == 1) {
                        vorstellung.setStartuhrzeit("12:00");
                    } if(i == 2) {
                        vorstellung.setStartuhrzeit("15:00");
                    } if(i == 3) {
                        vorstellung.setStartuhrzeit("18:00");
                    } if(i == 4) {
                        vorstellung.setStartuhrzeit("21:00");
                    }
                    vorstellung.setFilmName(omdbRepository.findAll().get(j).getTitel());
                    vorstellung.setLaengeDerVorstellungInMinuten("175");
                    vorstellung.setKinosaalNummer(i);
                    vorstellungController.vorstellungAnlegen(vorstellung);
                }
            }

            System.out.println("Vorstellung (und Sitzplan für die Vorstellung) eingefügt");
        }

    @RequestMapping(value ="/filme", produces = "application/json", method = RequestMethod.POST)
    @PostMapping(value ="/filme", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void testdatenFilmeAnlegen() {

        OMDBController omdbController = new OMDBController(omdbRepository);
        try {
            omdbController.filmeEinfuegen();
            System.out.println("Filme eingefügt!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @RequestMapping(value ="/kino", produces = "application/json", method = RequestMethod.POST)
    @PostMapping(value ="/kino", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void testdatenkinoAnlegen() {
        Kinos unserKino = new Kinos();
        unserKino.setStrasse("N7");
        unserKino.setHausnummer("17");
        unserKino.setOrt("Mannheim");
        unserKino.setPlz("68159");
        kinoRepository.save(unserKino);
        System.out.println("Kino eingefügt");
    }

    @RequestMapping(value ="/kinosaal", produces = "application/json", method = RequestMethod.POST)
    @PostMapping(value ="/kinosaal", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void testdatenkinoSaalAnlegen() {
        int gewuenschteKinoID = 0;
        ArrayList<Kinos> alleKinos = (ArrayList<Kinos>) kinoRepository.findAll();
        for(int i = 0; i < alleKinos.size(); i++) {
            if(alleKinos.get(i).getStrasse().equals("N7") && alleKinos.get(i).getHausnummer().equals("17")){
                gewuenschteKinoID = alleKinos.get(i).getKinoID();
            }
        }
        for(int j = 1; j <= 4; j++) {
            KinoSaale kinoSaal = new KinoSaale();
            kinoSaal.setKinoSaalID(j);
            kinoSaal.setKinoID(gewuenschteKinoID);
            kinoSaalRepository.save(kinoSaal);
        }
        System.out.println("Kinosaal eingefügt");
    }



    @RequestMapping(value ="/alle", produces = "application/json", method = RequestMethod.POST)
    @PostMapping(value ="/alle", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void testdatenAnlegen() {
        testdatenKinosaalplaetzeAnlegen();
        testdatenkinoAnlegen();
        testdatenkinoSaalAnlegen();
        testdatenFilmeAnlegen();
        testdatenVorstellungenAnlegen();
        sitzplanFuerSitzplaetzeAnlegen();
        System.err.println("TESTDATEN ANGELEGT!");
    }


    public void sitzplanFuerSitzplaetzeAnlegen() {
        ArrayList<Vorstellungen> alleVorstellungen = (ArrayList<Vorstellungen>) vorstellungRepository.findAll();
        ArrayList<Sitzplaetze> alleSitzplaetze = (ArrayList<Sitzplaetze>) sitzplaetzeRepository.findAll();
        ArrayList<KinoSaale> alleKinosaale = (ArrayList<KinoSaale>) kinoSaalRepository.findAll();
        sitzplaetzeFuerVorstellungRepository.deleteAll();
        for(int i = 0; i < alleVorstellungen.size(); i++) {
            int kinosaalnummer = alleVorstellungen.get(i).getKinosaalNummer();
            for(int j = 0; j < alleSitzplaetze.size(); j++) {
                ArrayList<Sitzplaetze> benoetigteSitzplaetze = new ArrayList<>();
                if(alleSitzplaetze.get(j).getKinosaalID() == kinosaalnummer) {
                    SitzplaetzeFuerVorstellung sitzplaetzeFuerVorstellung = new SitzplaetzeFuerVorstellung(0, alleSitzplaetze.get(j).getReihe(), alleSitzplaetze.get(j).getSpalte(), alleVorstellungen.get(i).getVorstellungsid(), "FREI");
                    sitzplaetzeFuerVorstellungRepository.save(sitzplaetzeFuerVorstellung);
                }
            }
        }

    }



}

