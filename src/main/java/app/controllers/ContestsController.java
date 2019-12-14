package app.controllers;

import org.javalite.activeweb.AppController;
import org.javalite.activeweb.annotations.DELETE;
import org.javalite.activeweb.annotations.GET;
import org.javalite.activeweb.annotations.POST;

import app.controllers.helpers.ControllerHelper;
import app.models.Contest;
import app.models.Participant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class ContestsController extends AppController {                

    private static final String ATTR_PARTICIPANT_CONTEST_ID = "contest_id";
    private static final String ATTR_ID = "id";
    private static final String ATTR_CONTEST_NAME = "name";
    private static final String PARTICIPANT_ROW_HTML_ID_PREFIX = "r";
    private static final String VIEW_PARAMS = "params";
    private static final String VIEW_MESSAGE = "message";
    private static final String VIEW_PARTICIPANTS = "participants";
    private static final String VIEW_ERRORS = "errors";
    private static final String VIEW_CONTEST = "contest";
    private static final String VIEW_CONTESTS = "contests";
    private static final String SESSION_CONTEST_ERROR = "contestError";
    private static final String SESSION_PARTICIPANTS_SIZE = "participantsSize";

    public void index() {
        view(VIEW_CONTESTS, Contest.findAll().toMaps());
    }

    public void show() {
        //this is to protect from URL hacking
        Contest b = (Contest) Contest.findById(getId());
        if(b != null) {
            session(SESSION_PARTICIPANTS_SIZE, Integer.valueOf(b.getAll(Participant.class).size()));
            view(VIEW_CONTEST, b);
        } else {
            view(VIEW_MESSAGE, "are you trying to hack the URL?");
            render("/system/404");
        }
    }

    public void editForm() {
        //this is to protect from URL hacking
        Contest b = (Contest) Contest.findById(getId());
        if(b != null) {
            session(SESSION_PARTICIPANTS_SIZE, Integer.valueOf(b.getAll(Participant.class).size()));
            Object bError = (Contest) session(SESSION_CONTEST_ERROR);
            if (bError instanceof Contest) {
                view(VIEW_ERRORS, ((Contest)bError).errors());
                view(VIEW_CONTEST, bError);
                session(SESSION_CONTEST_ERROR, null);
            } else {
                List<Participant> participants = b.getAll(Participant.class);
                view(VIEW_CONTEST, b);
                view(VIEW_PARTICIPANTS, participants);
            }
        } else {
            view(VIEW_MESSAGE, "are you trying to hack the URL?");
            render("/system/404");
        }
    }


    private Map<Integer, Participant> getParticipantsMappedById(List<Participant> participants) {
        Map<Integer, Participant> participantsMap = new HashMap<Integer, Participant>();
        for (Participant participant : participants) {
            Integer id = (Integer) participant.getId();
            if (id != null) {
                participantsMap.put(id, participant);
            }
        }
        return participantsMap;
    }
  
    @POST
    public void save() {
        //this is to protect from URL hacking
        Contest b = (Contest) Contest.findById(getId());
        if(b != null) {
            List<Participant> currentParticipants = b.getAll(Participant.class);
            b.fromMap(params1st());
            Map<Integer, Participant> participantsMap = getParticipantsMappedById(currentParticipants);
            List<String> rowIndexes = ControllerHelper.getRowIndexes(params1st(), PARTICIPANT_ROW_HTML_ID_PREFIX, ATTR_ID, true, false);
            for (String rowIndex : rowIndexes) {
                Map<String, String> paramMap = ControllerHelper.getParamMap(params1st(), PARTICIPANT_ROW_HTML_ID_PREFIX, rowIndex);
                String idStr = paramMap.get(ATTR_ID);
                try {
                    Integer id = Integer.valueOf(idStr);
                    if (participantsMap.containsKey(id)) {
                        Participant participant = participantsMap.get(id);
                        participant.fromMap(paramMap);
                    }
                } catch (NumberFormatException ex) {
                    // ignore
                }
            }
            List<Participant> newParticipantsList = getNewParticipantsList(b);
            if(!b.save()) {
                flash(VIEW_MESSAGE, "Something went wrong, please  fill out all fields");
                session(SESSION_CONTEST_ERROR, b);
                // after post always redirect to support browser-back
                redirect(ContestsController.class, "edit_form", getId());
                // render("edit_form");
            } else {
                
                for (Participant p : currentParticipants) {
                    // TODO: check errors
                    p.save();
                }
                for (Participant p : newParticipantsList) {
                    // TODO: check errors
                    p.save();
                }


                flash(VIEW_MESSAGE, "Contest updated: " + b.get(ATTR_CONTEST_NAME));
                redirect(ContestsController.class);
            }
        } else {
            view(VIEW_MESSAGE, "are you trying to hack the URL?");
            render("/system/404");
        }
    }

    private List<Participant> getNewParticipantsList(Contest b) {
        List<Participant> newParticipantsList = new ArrayList<Participant>();
        List<String> rowNewIndexes = ControllerHelper.getRowIndexes(params1st(), PARTICIPANT_ROW_HTML_ID_PREFIX, ATTR_ID, false, true);
        for (String rowIndex : rowNewIndexes) {
            Map<String, String> paramMap = ControllerHelper.getParamMap(params1st(), PARTICIPANT_ROW_HTML_ID_PREFIX, rowIndex);
            Participant participant = new Participant();
            participant.fromMap(paramMap);
            participant.setId(null);    // set null, not the empty-string (which causes update instead of insert)
            participant.setInteger(ATTR_PARTICIPANT_CONTEST_ID, b.getId()); // link it to this contest
            newParticipantsList.add(participant);
        }
        return newParticipantsList;
    }
    
    @POST
    public void create() {
        Contest Contest = new Contest();
        Contest.fromMap(params1st());
        if(!Contest.save()) {
            flash(VIEW_MESSAGE, "Something went wrong, please  fill out all fields");
            flash(VIEW_ERRORS, Contest.errors());
            flash(VIEW_PARAMS, params1st());
            redirect(ContestsController.class, "new_form");
        } else {
            flash(VIEW_MESSAGE, "New Contest was added: " + Contest.get(ATTR_CONTEST_NAME));
            redirect(ContestsController.class);
        }
    }

    @GET
    public void addParticipant() {
        int newRowIndex = (Integer) session(SESSION_PARTICIPANTS_SIZE);
        view("row", PARTICIPANT_ROW_HTML_ID_PREFIX + newRowIndex);
        newRowIndex++;
        session(SESSION_PARTICIPANTS_SIZE, Integer.valueOf(newRowIndex));
        render("/contests/_participant").noLayout();
    }

    @DELETE
    public void delete() {

        Contest b = (Contest)Contest.findById(getId());
        String name = b.getString(ATTR_CONTEST_NAME);
        b.delete();
        flash(VIEW_MESSAGE, "Contest: '" + name + "' was deleted");
        redirect(ContestsController.class);
    }

    public void newForm() {}
}
