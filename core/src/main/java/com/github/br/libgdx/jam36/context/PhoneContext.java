package com.github.br.libgdx.jam36.context;

public class PhoneContext {

    private String callerName;

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallerName() {
        return callerName;
    }

    // listener
    public interface Listener {
        void update(PhoneContext context);
    }


    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void notifyListener() {
        if (listener != null) {
            listener.update(this);
        }
    }

}
