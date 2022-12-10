package GUI.Controller;

import GUI.Model.MyTunesModel;

public abstract class BaseController {
    private MyTunesModel model;

    /**
     * Sets the model of the controller
     * @param model
     */
    public void setModel(MyTunesModel model) {
        this.model = model;
    }

    /**
     * Get the model of the controller
     * @return the model of the controller
     */
    public MyTunesModel getModel() {
        return model;
    }

    /**
     * What happens when the controller starts
     */
    public abstract void setup();
}
