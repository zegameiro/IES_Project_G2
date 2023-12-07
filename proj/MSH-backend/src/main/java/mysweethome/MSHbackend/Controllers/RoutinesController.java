package mysweethome.MSHbackend.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import mysweethome.MSHbackend.Models.*;
import mysweethome.MSHbackend.Repositories.OutputDeviceRepository;
import mysweethome.MSHbackend.Services.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(path = "/routines")
@Tag(name = "Routine and Action Endpoints")
public class RoutinesController {

    @Autowired
    private RoutineService routines;

    @Autowired
    private ActionsService actions;

    @Autowired
    private OutputDeviceRepository outputDeviceRepository;

    @Operation(summary = "Add a new action", description = "Add a new action to a specific output device")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns a OK string"), 
        @ApiResponse(responseCode = "422", description = "An output device with the specified ID does not exist!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "The specified action is not supported!",  content = @Content),
        @ApiResponse(responseCode = "422", description = "The specified action is not applicable to the specified output device!",  content = @Content)
    })
    @PostMapping("/add")
    public @ResponseBody String addRoutine(@RequestBody Routine routine) {


        System.out.println(routine.toString());

        Action act = routine.getAssociated_action();

        if (act == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action not found.");
        }

        String associated_device_id = act.getOutputDeviceID(); // device associated to this action

        if (outputDeviceRepository.findById(associated_device_id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Output device not found.");
        }

        String action_description = act.getAction_description();

        OutputDevice device = outputDeviceRepository.findById(associated_device_id).get();

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Output device not found.");
        }

        // check if action is supported by this device
        if (!device.getDevice_category().getPossibleActions().contains(action_description)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action not supported by this device.");
        }

        act.setTimestamp(System.currentTimeMillis());

        routines.saveRoutine(routine);
        actions.saveAction(act);

        return "Routine added successfully.";
    }

    @GetMapping("/check")
    public @ResponseBody List<String> checkActions(@RequestParam String device_id){

        OutputDevice device = outputDeviceRepository.findById(device_id).get();

        if (device == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Output device not found.");
        }

        return device.getDevice_category().getPossibleActions();

    }

}
