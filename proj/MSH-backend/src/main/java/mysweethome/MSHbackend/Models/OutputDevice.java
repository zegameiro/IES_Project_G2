package mysweethome.MSHbackend.Models;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("outputdevices")
public class OutputDevice {

    @Id
    private int device_id;
    @NotBlank
    private OutputDeviceType device_category;
    @NotBlank
    private String device_location;
    @NotBlank
    private String current_state;
    @NotBlank
    ArrayList<String> permited_actions; // actions that can be performed on this device

}
