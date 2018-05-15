package com.androb.androidrobot.utils.messageUtil;

/**
 * Created by kaki on 2018/05/14.
 */

public class MessageValidator {

    public boolean checkMessage(String answerStr, String quesType, String quesId) {
        int qid = Integer.valueOf(quesId);

        switch (quesType) {
            case "code":
                return this.checkCodeAnswer(answerStr, qid);

            case "graph":
                return this.checkGraphAnswer(answerStr, qid);

            case "drag":
                return this.checkDragAnswer(answerStr, qid);
        }

        return false;
    }


    private boolean checkDragAnswer(String answer, int quesId) {
        boolean result = false;
        switch(quesId) {
            case 1:
                if (answer.equals("{\"1\":\"4\",\"2\":\"90\",\"0\":\"3\"}")) {
                    System.out.println("true");
                    result = true;
                } else {
                    System.out.println("false");
                    result = false;
                }
                break;
            case 2:
                if (answer.equals("{\"1\":\"2\",\"4\":\"3\",\"5\":\"1\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 3:
                if (answer.equals("{\"1\":\"2\",\"4\":\"3\",\"5\":\"1\"}") || answer.equals("{\"10\":\"{\"4\":\"{\"1\":\"5\",\"3\":\"90\"}\"}\"}")
                        || answer.equals("{\"10\":\"{\"4\":\"{\"2\":\"90\",\"1\":\"5\"}\"}\"}") || answer.equals("{\"10\":\"{\"4\":\"{\"3\":\"90\",\"1\":\"5\"}\"}\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 4:
                if (answer.equals("{\"0\":\"6\",\"10\":\"{\"5\":\"{\"5\":\"1\"}\"}\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
        }
        return result;
    }


    private boolean checkGraphAnswer(String answer, int questionId) {
        boolean result = false;
        switch(questionId) {
            case 1:
                if (answer.equals("{\"1\":\"4\",\"2\":\"90\",\"0\":\"3\"}")) {
                    System.out.println("true");
                    result = true;
                } else {
                    System.out.println("false");
                    result = false;
                }
                break;
            case 2:
                if (answer.equals("{\"1\":\"2\",\"4\":\"3\",\"5\":\"1\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 3:
                if (answer.equals("{\"10\":{\"4\":{\"1\":\"5\",\"2\":\"90\"}}}") || answer.equals("{\"10\":{\"4\":{\"1\":\"5\",\"3\":\"90\"}}}")
                        || answer.equals("{\"10\":{\"4\":{\"2\":\"90\",\"1\":\"5\"}}}") || answer.equals("{\"10\":{\"4\":{\"3\":\"90\",\"1\":\"5\"}}}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 4:
                if (answer.equals("{}") || answer.equals("{\"1\":\"10\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
        }
        return result;
    }


    private boolean checkCodeAnswer(String answer, int questionId) {
        boolean result = false;
        switch(questionId) {
            case 1:
                if (answer.equals("{\"1\":\"4\",\"2\":\"90\",\"0\":\"3\"}")) {
                    System.out.println("true");
                    result = true;
                } else {
                    System.out.println("false");
                    result = false;
                }
                break;
            case 2:
                if (answer.equals("{\"1\":\"2\",\"4\":\"3\",\"5\":\"1\"}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 3:
                if (answer.equals("{\"10\":{\"5\":{\"1\":\"2\",\"4\":\"3\"}}}")) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case 4:
                if ( (answer.equals("{\"5\":\"1\",\"10\":{\"4\":{\"1\":\"3\",\"3\":\"90\"}}}"))
                        || (answer.equals("{\"5\":\"1\",\"10\":{\"4\":{\"1\":\"3\",\"2\":\"90\"}}}")) ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
        }
        return result;
    }
}
