package kz.informatics.labirintoiyn;

public class Question {
    String question, q1,q2,q3,q4,q5;

    public Question(String question, String q1, String q2, String q3) {
        this.question = question;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
    }

    public String getQ() {
        return question;
    }
    public String getV1() {
        return q1;
    }
    public String getV2() {
        return q2;
    }
    public String getV3() {
        return q3;
    }

}
