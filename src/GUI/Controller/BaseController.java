package GUI.Controller;

import GUI.Model.MyTunesModel;

public abstract class BaseController {
    private MyTunesModel model;

    public void setModel(MyTunesModel model) {
        this.model = model;
    }

    public MyTunesModel getModel() {
        return model;
    }

    public abstract void setup();
}
