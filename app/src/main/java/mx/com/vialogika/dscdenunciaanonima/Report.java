package mx.com.vialogika.dscdenunciaanonima;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mx.com.vialogika.dscdenunciaanonima.BR;

public class Report extends BaseObservable {

    private String UUID = java.util.UUID.randomUUID().toString();
    private String dateTime = "";
    private String eventDateTime;
    private String title;
    private String subject = "";
    private String what;
    private String when;
    private String how;
    private String where;
    private String description = "";
    private boolean couldBeContacted = false;
    private List<String> evidencePaths = new ArrayList<>();

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getHow() {
        return how;
    }

    public void setHow(String how) {
        this.how = how;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCouldBeContacted() {
        return couldBeContacted;
    }

    public void setCouldBeContacted(boolean couldBeContacted) {
        this.couldBeContacted = couldBeContacted;
    }

    @Bindable
    public List<String> getEvidencePaths() {
        return evidencePaths;
    }

    public void setEvidencePaths(List<String> evidencePaths) {
        this.evidencePaths = evidencePaths;
        notifyPropertyChanged(BR.evidencePaths);
    }

    public void addPath(String path){
        this.evidencePaths.add(path);
        notifyPropertyChanged(BR.evidencePaths);
    }

    public void removePath(int position){
        this.evidencePaths.remove(position);
        notifyPropertyChanged(BR.evidencePaths);
    }
}
