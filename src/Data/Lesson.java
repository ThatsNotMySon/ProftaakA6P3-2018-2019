package Data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Lesson implements Serializable {

    private LocalDateTime startTime;
    private int duration;
    private String teacher;
    private String subject;

    public Lesson(LocalDateTime startTime, int duration, String teacher, String subject) {
        this.startTime = startTime;
        this.duration = duration;
        this.teacher = teacher;
        this.subject = subject;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                ", teacher='" + teacher + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
