package main.sample;

public class Game {
    String userName;
    String opponentName;
    int userScore;
    int opponentScore;
    int round = 1;
    boolean gameIsFinished = false;

    public Game() {

    }

    private void calculateScore(String winner) {
        String temp = winner;
        if (winner.equals("opponentName")) {
            opponentScore++;
        } else if (winner.equals("userName")) {
            userScore++;
        }
        if (!winner.equals("draw")) {
            round++;
        }
    }

    public int getUserScore() {
        return userScore;
    }


    public int getOpponentScore() {
        return opponentScore;
    }

    public String calculateFinalWinner(userName, opponentName) {
        if (this.userScore > this.opponentScore) {
            return userName;
        } else {

            return opponentName;
        }

    }

    public void reset() {
        userScore = 0;
        opponentScore = 0;
        round = 0;
        gameIsFinished = false;
    }

    public int getRound() {
        return round;
    }

    public boolean gameIsFinished() {
        return gameIsFinished;
    }

    public void calculateWinner(String userName, String opponentName) {
        this.opponentName = opponentName;
        this.userName = userName;
        if (gameIsFinished == false) {

            if (opponentName.equals("rock") && userName.equals("paper")) {
                calculateScore(userName);
            }

            if (opponentName.equals("rock") && userName.equals("scissors")) {
                calculateScore(opponentName);
            }

            if (opponentName.equals("rock") && userName.equals("rock")) {
                calculateScore("draw");
            }

            if (opponentName.equals("paper") && userName.equals("rock")) {
                calculateScore(opponentName);
            }

            if (opponentName.equals("paper") && userName.equals("papar")) {
                calculateScore("draw");
            }

            if (opponentName.equals("paper") && userName.equals("scissors")) {
                calculateScore(userName);
            }

            if (opponentName.equals("scissors") && userName.equals("rock")) {
                calculateScore(userName);
            }

            if (opponentName.equals("scissors") && userName.equals("papar")) {
                calculateScore(opponentName);
            }

            if (opponentName.equals("scissors") && userName.equals("scissors")) {
                calculateScore("draw");
            }
        }
        if (round == 5) {
            calculateFinalWinner(userName, opponentName);
            gameIsFinished = true;
        }
    }
}
