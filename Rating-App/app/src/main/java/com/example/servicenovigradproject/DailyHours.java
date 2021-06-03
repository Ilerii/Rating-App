package com.example.servicenovigradproject;

public class DailyHours {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    /**
     *
     * @param startHour int representing the hour the location opens. in range -1 - 23,
     * @param startMinute int representing the minute the location opens. in range -1 - 59
     * @param endHour int representing the hour the location closes. in range -1 - 23,
     * @param endMinute int representing the minute the location closes. in range -1 - 59
     * -1 for all values = closed
     */
    public DailyHours(int startHour, int startMinute, int endHour, int endMinute){
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }
    public DailyHours(){ }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public void setStartMinute(int startMinute){
        this.startMinute = startMinute;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    public void setEndMinute(int endMinute){
        this.endMinute = endMinute;
    }
    public int getStartHour(){
        return startHour;
    }
    public int getStartMinute(){
        return startMinute;
    }
    public int getEndHour(){
        return endHour;
    }
    public int getEndMinute(){
        return endMinute;
    }

    @Override
    public String toString() {
        return "DailyHours{" +
                "start=" + startHour + ":" + startMinute +
                "end=" + endHour + ":" + endMinute +
                '}';
    }
}
