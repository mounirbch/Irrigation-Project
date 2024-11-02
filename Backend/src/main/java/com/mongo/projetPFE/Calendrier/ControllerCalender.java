package com.mongo.projetPFE.Calendrier;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import com.mongo.projetPFE.notif.NotifRepo;
import com.mongo.projetPFE.notif.notifEntity;
import com.mongo.projetPFE.security.JwtFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendar")
public class ControllerCalender {
    @Autowired
    private ServiceCalender serviceCalender;
    @Autowired
    private RepositoryCalender repositoryCalender;
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private NotifRepo notifRepo;



    @PostMapping("/add")
    public ResponseEntity<Object> addEvent(@RequestBody EntityCalender event) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        event.setUtilisateur(utilisateur);

        Date now = new Date();

        // Check if the start date is before the current date and time
        if (event.getStart().before(now)) {
            return new ResponseEntity<>("La date de début doit être ultérieure à l'heure actuelle", HttpStatus.BAD_REQUEST);
        }

        if (event.getStart().after(event.getEnd())) {
            return new ResponseEntity<>("La date de début doit être antérieure à la date de fin", HttpStatus.BAD_REQUEST);
        }

       /* List<EntityCalender> eventsAfterStart = repositoryCalender.findByEndGreaterThanEqualAndUtilisateur(event.getStart(), utilisateur);
        List<EntityCalender> eventsBeforeEnd = repositoryCalender.findByStartLessThanEqualAndUtilisateur(event.getEnd(), utilisateur);


        if (!eventsAfterStart.isEmpty() &&! eventsBeforeEnd.isEmpty()) {
            return new ResponseEntity<>("L'événement n'a pas été ajouté car il y a un chevauchement", HttpStatus.CONFLICT);
        }*/
        List<EntityCalender> eventsBetweenStartAndEnd = repositoryCalender.findByStartBetweenAndUtilisateur(event.getStart(), event.getEnd(), utilisateur);
        List<EntityCalender> eventsBetweenEndAndStart = repositoryCalender.findByEndBetweenAndUtilisateur(event.getStart(), event.getEnd(), utilisateur);
        List<EntityCalender> eventsContainingStartOrEnd = repositoryCalender.findByStartLessThanEqualAndEndGreaterThanEqualAndUtilisateur(event.getStart(), event.getEnd(), utilisateur);



        if (!eventsBetweenStartAndEnd.isEmpty() || !eventsBetweenEndAndStart.isEmpty() || !eventsContainingStartOrEnd.isEmpty()) {
            return new ResponseEntity<>("L'événement n'a pas été ajouté car il y a un chevauchement", HttpStatus.CONFLICT);
        }

        convertDatesToUTC(event);

        EntityCalender addedEvent = serviceCalender.addEvent(event);
        return new ResponseEntity<>("évenement ajouté", HttpStatus.CREATED);
    }

    //2eme methode:
    /*@PostMapping("/add")
    public ResponseEntity<Object> addEvent(@RequestBody EntityCalender event) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        event.setUtilisateur(utilisateur);
        Optional<EntityCalender> existingEventOptional = repositoryCalender.findByTitleAndUtilisateur(event.getTitle(), utilisateur);

        if (existingEventOptional.isPresent()) {
            // Un événement avec le même titre existe déjà, renvoyer une erreur
            return new ResponseEntity<>("Un événement avec le même titre existe déjà ", HttpStatus.CONFLICT);
        }



        if (event.getStart().after(event.getEnd())) {
            return new ResponseEntity<>("La date de début doit être antérieure à la date de fin", HttpStatus.BAD_REQUEST);
        }

       /* List<EntityCalender> eventsAfterStart = repositoryCalender.findByEndGreaterThanEqualAndUtilisateur(event.getStart(), utilisateur);
        List<EntityCalender> eventsBeforeEnd = repositoryCalender.findByStartLessThanEqualAndUtilisateur(event.getEnd(), utilisateur);


        if (!eventsAfterStart.isEmpty() &&! eventsBeforeEnd.isEmpty()) {
            return new ResponseEntity<>("L'événement n'a pas été ajouté car il y a un chevauchement", HttpStatus.CONFLICT);
        }*/
        /*List<EntityCalender> eventsBetweenStartAndEnd = repositoryCalender.findByStartBetweenAndUtilisateur(event.getStart(), event.getEnd(), utilisateur);
        List<EntityCalender> eventsBetweenEndAndStart = repositoryCalender.findByEndBetweenAndUtilisateur(event.getStart(), event.getEnd(), utilisateur);
        List<EntityCalender> eventsContainingStartOrEnd = repositoryCalender.findByStartLessThanEqualAndEndGreaterThanEqualAndUtilisateur(event.getStart(), event.getEnd(), utilisateur);



        if (!eventsBetweenStartAndEnd.isEmpty() || !eventsBetweenEndAndStart.isEmpty() || !eventsContainingStartOrEnd.isEmpty()) {
            return new ResponseEntity<>("L'événement n'a pas été ajouté car il y a un chevauchement", HttpStatus.CONFLICT);
        }

        convertDatesToUTC(event);

        EntityCalender addedEvent = serviceCalender.addEvent(event);
        return new ResponseEntity<>("évenement ajouté", HttpStatus.CREATED);
    }*/


    //fin 2eme methode

    //methode3
   /* @PostMapping("/add")
    public ResponseEntity<Object> addEvent(@RequestBody EntityCalender event) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        event.setUtilisateur(utilisateur);
        Optional<EntityCalender> existingEventOptional = repositoryCalender.findByTitleAndUtilisateur(event.getTitle(), utilisateur);

        if (existingEventOptional.isPresent()) {
            // Un événement avec le même titre existe déjà, renvoyer une erreur
            return new ResponseEntity<>("Un événement avec le même titre existe déjà ", HttpStatus.CONFLICT);
        }

        if (event.getStart().after(event.getEnd())) {
            return new ResponseEntity<>("La date de début doit être antérieure à la date de fin", HttpStatus.BAD_REQUEST);
        }

        //List<EntityCalender> eventsAfterStart = repositoryCalender.findByEndGreaterThanEqual(event.getStart());
        //List<EntityCalender> eventsBeforeEnd = repositoryCalender.findByStartLessThanEqual(event.getEnd());


        // Récupérer tous les événements existants
        Iterable<EntityCalender> allEvents = repositoryCalender.findByUtilisateur(utilisateur);
        // Vérifier si l'événement se chevauche avec un événement existant
        for (EntityCalender existingEvent : allEvents) {
            if ((event.getStart().compareTo(existingEvent.getStart()) >= 0 && event.getStart().compareTo(existingEvent.getEnd()) <= 0) ||
                    (event.getEnd().compareTo(existingEvent.getStart()) >= 0 && event.getEnd().compareTo(existingEvent.getEnd()) <= 0) ||
                    (event.getStart().compareTo(existingEvent.getStart()) <= 0 && event.getEnd().compareTo(existingEvent.getEnd()) >= 0)) {
                return new ResponseEntity<>("L'événement n'a pas été ajouté car il y a un chevauchement", HttpStatus.CONFLICT);
            }
        }


        convertDatesToUTC(event);

        EntityCalender addedEvent = serviceCalender.addEvent(event);
        return new ResponseEntity<>("évenement ajouté", HttpStatus.CREATED);
    }*/





















    private void convertDatesToUTC(EntityCalender event) {

        TimeZone currentTimeZone = TimeZone.getDefault();

        event.setStart(convertToUTC(event.getStart(), currentTimeZone));
        event.setEnd(convertToUTC(event.getEnd(), currentTimeZone));
    }

    private Date convertToUTC(Date date, TimeZone timeZone) {

        int offset = timeZone.getRawOffset();

        return new Date(date.getTime() - offset);
    }
    @PostMapping("/add1")
    public ResponseEntity<String> addEvent1(@RequestBody EntityCalender event) {
        EntityCalender addedEvent = serviceCalender.addEvent(event);

        return new ResponseEntity<>("events ajouté", HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public Iterable<EntityCalender> get() {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        return repositoryCalender.findByUtilisateur(utilisateur);
    }

    @GetMapping("/get1")
    public List<EventDate> get1(HttpServletRequest request) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        Iterable<EntityCalender> userEventsIterable = repositoryCalender.findByUtilisateur(utilisateur);

        Iterable<EntityCalender> systemEventsIterable = repositoryCalender.findByUtilisateurId("Modéle Machine Learning");

        List<EntityCalender> events = new ArrayList<>();
        userEventsIterable.forEach(events::add);
        systemEventsIterable.forEach(events::add);

        return events.stream()
                .map(event -> new EventDate(event.getTitle(), event.getStart(), event.getEnd()))
                .collect(Collectors.toList());
    }


    public static class EventDate {
        private String title;
        private Date start;
        private Date end;

        public EventDate(String title, Date start, Date end) {
            this.title = title;
            this.start = start;
            this.end = end;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }
    }



    @GetMapping("/get/{eventName}")
    public ResponseEntity<Object> getEventByName(@PathVariable String eventName) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        Optional<EntityCalender> eventOptional = repositoryCalender.findByTitleAndUtilisateur( eventName,  utilisateur);

        if (eventOptional.isPresent()) {
            return new ResponseEntity<>(eventOptional, HttpStatus.OK);
        } else {
            String errorMessage = "Événement " + eventName + " introuvable.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{eventName}")
    public ResponseEntity<String> deleteEventByName(@PathVariable String eventName) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        Optional<EntityCalender> eventOptional = repositoryCalender.findByTitleAndUtilisateur(eventName , utilisateur);

        if (eventOptional.isPresent()) {
            repositoryCalender.delete(eventOptional.get());
            return new ResponseEntity<>("Évenement Supprimé", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Évenement n'existe pas", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update/{title}")
    public ResponseEntity<String> updateEvent(@PathVariable String title, @RequestBody EntityCalender updatedEvent) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        Optional<EntityCalender> existingEventOptional = repositoryCalender.findByTitleAndUtilisateur(title,utilisateur);

        if (existingEventOptional.isPresent()) {
            EntityCalender existingEvent = existingEventOptional.get();


            if (updatedEvent.getStart().before(updatedEvent.getEnd())) {
                existingEvent.setStart(updatedEvent.getStart());
                existingEvent.setEnd(updatedEvent.getEnd());

                repositoryCalender.save(existingEvent);

                return new ResponseEntity<>("Événement modifié", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("La date de début doit être antérieure à la date de fin", HttpStatus.BAD_REQUEST);
            }


        } else {
            return new ResponseEntity<>("Évenement n'existe pas", HttpStatus.NOT_FOUND);
        }


    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEventByDates(
            @RequestBody EntityCalender request
    ) {
        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        Date start = request.getStart();
        Date end = request.getEnd();

        Optional<EntityCalender> userEventOptional = repositoryCalender.findByStartAndEndAndUtilisateur(start, end, utilisateur);

        Utilisateur systemUser = new Utilisateur();
        systemUser.setId("Modéle Machine Learning");
        Optional<EntityCalender> systemEventOptional = repositoryCalender.findByStartAndEndAndUtilisateur(start, end, systemUser);

        if (userEventOptional.isPresent()) {
            repositoryCalender.delete(userEventOptional.get());
            return new ResponseEntity<>("Événement de l'utilisateur supprimé avec succès", HttpStatus.OK);
        }
        else if (systemEventOptional.isPresent()) {
            repositoryCalender.delete(systemEventOptional.get());
            return new ResponseEntity<>("Événement géneré par l'IA supprimé avec succès", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Événement non trouvé", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/modifier")
    public ResponseEntity<Object> ModifierEventByDates(
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "start", required = true) String startString,
            @RequestParam(value = "end", required = true) String endString,
            @RequestBody EntityCalender request
    ) {
        Date start = parseDateString(startString);
        Date end = parseDateString(endString);



        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        Optional<EntityCalender> eventOptional = repositoryCalender.findByStartAndEndAndUtilisateur(start, end, utilisateur);
        if (request.getStart().after(request.getEnd())) {
            return new ResponseEntity<>("La date de début doit être antérieure à la date de fin", HttpStatus.BAD_REQUEST);
        }
        Date now = new Date();
        if (request.getStart().before(now)) {
            return new ResponseEntity<>("La date de début doit être ultérieure à l'heure actuelle", HttpStatus.BAD_REQUEST);
        }


        if (eventOptional.isPresent()) {
            EntityCalender event = eventOptional.get();
/*
            repositoryCalender.delete(eventOptional.get());
            List<EntityCalender> eventsAfterStart = repositoryCalender.findByEndGreaterThanEqualAndUtilisateur(request.getStart(),utilisateur);
            List<EntityCalender> eventsBeforeEnd = repositoryCalender.findByStartLessThanEqualAndUtilisateur(request.getEnd(),utilisateur);

            if (!eventsAfterStart.isEmpty() && !eventsBeforeEnd.isEmpty()) {
                return new ResponseEntity<>("L'événement n'a pas été modifié car il y a un chevauchement", HttpStatus.CONFLICT);
            }

 */



            convertDatesToUTC(request);
            event.setTitle(title);
            event.setStart(request.getStart());
            event.setEnd(request.getEnd());
            EntityCalender addedEvent = serviceCalender.addEvent(event);
            return new ResponseEntity<>("évenement modfié", HttpStatus.CONFLICT);}        else {
            return new ResponseEntity<>("Oups! vous n'avez pas le droit de modifié", HttpStatus.NOT_FOUND);        }
    }

    private Date parseDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Date parseDateString1(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = dateFormat.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, -1);

            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/getByInterval")
    public ResponseEntity<List<EntityCalender>> getEventsByInterval(
            @RequestParam(value = "start", required = true) String startString,
            @RequestParam(value = "end", required = false) String endString
    ) {        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();

        Date start = parseDateString1(startString);
        List<EntityCalender> events;
        if (endString!=null){
        Date end = parseDateString1(endString);
         events = repositoryCalender.findAllByStartBetweenAndUtilisateur(start, end, utilisateur);}
        else{
            events = repositoryCalender.findByStartGreaterThanEqualAndUtilisateur(start, utilisateur);}



        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    @GetMapping("/tempsDirrigation")
    public int checkEventTimeInterval() {

        int result = serviceCalender.checkEventTimeInterval();

      return result;
    }


   /* @GetMapping("/getByInterval")
    public ResponseEntity<List<EntityCalender>> getEventsByInterval(
            @RequestParam(value = "start", required = true) String startString,
            @RequestParam(value = "end", required = false) String endString
    ) {        Utilisateur utilisateur = jwtFilter.getCurrentAuthenticatedUtilisateur();
        Utilisateur systemUser = new Utilisateur();
        systemUser.setId("Modéle Machine Learning");

        Date start = parseDateString1(startString);
        List<EntityCalender> events = new ArrayList<>();
        if (endString!=null){
            Date end = parseDateString1(endString);
            events.addAll(repositoryCalender.findAllByStartBetweenAndUtilisateur(start, end, utilisateur));
            events.addAll(repositoryCalender.findAllByStartBetweenAndUtilisateur(start, end, systemUser));
        }
        else{
            events.addAll(repositoryCalender.findByStartGreaterThanEqualAndUtilisateur(start, utilisateur));
            events.addAll(repositoryCalender.findByStartGreaterThanEqualAndUtilisateur(start, systemUser));
        }



        return new ResponseEntity<>(events, HttpStatus.OK);
    }
*/


}
