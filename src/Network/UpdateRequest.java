package  Network;

import LabWorks.LabWork;
import java.io.Serializable;

public class UpdateRequest implements Serializable {
    private Long id;
    private LabWork labWork;

    public UpdateRequest(Long id, LabWork labWork) {
        this.id = id;
        this.labWork = labWork;
    }

    public Long getId() {
        return id;
    }

    public LabWork getLabWork() {
        return labWork;
    }
}
