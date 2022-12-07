package GUI.Controller;

import GUI.Model.MyTunesModel;

public abstract class BaseController {
    private MyTunesModel model;

    /**
     * sets the model of the controller
     * @param model
     */
    public void setModel(MyTunesModel model) {
        this.model = model;
    }

    /**
     * get the model of the controller
     * @return the model of the controller
     */
    public MyTunesModel getModel() {
        return model;
    }

    /**
     * what happens when the controller starts
     */
    public abstract void setup();
}
