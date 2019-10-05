package com.sociorich.app.modals;

import java.io.Serializable;
import java.util.List;

public class UserCommentModal implements Serializable {
    private List<String> comentUser;
    private List<String> coment;
    private List<String> comentDate;

    public List<String> getComentUser() {
        return comentUser;
    }

    public void setComentUser(List<String> comentUser) {
        this.comentUser = comentUser;
    }

    public List<String> getComent() {
        return coment;
    }

    public void setComent(List<String> coment) {
        this.coment = coment;
    }

    public List<String> getComentDate() {
        return comentDate;
    }

    public void setComentDate(List<String> comentDate) {
        this.comentDate = comentDate;
    }
}
