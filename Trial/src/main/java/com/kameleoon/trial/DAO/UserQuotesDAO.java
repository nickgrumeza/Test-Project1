package com.kameleoon.trial.DAO;

import com.kameleoon.trial.models.Quote;
import com.kameleoon.trial.models.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class UserQuotesDAO {
    private static final String URL = "jdbc:sqlite:E:\\Programming\\Kameleoon\\Trial\\trialdb";
    private static Connection connection;
    private final Statement statement = connection.createStatement();

    private String SQL;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public UserQuotesDAO() throws SQLException {
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            SQL = "SELECT * FROM Users";
            ResultSet result = statement.executeQuery(SQL);
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setName(result.getString("name"));
                user.setEmail(result.getString("email"));
                user.setDateOfCreation(result.getDate("date"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public User getUser(int userId) {
        User user = new User();
        try {
            SQL = "SELECT * FROM Users WHERE id = " + userId;
            ResultSet result = statement.executeQuery(SQL);
            user.setId(result.getInt("id"));
            user.setName(result.getString("name"));
            user.setEmail(result.getString("email"));
            user.setDateOfCreation(result.getDate("date"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public List<Quote> getQuotes() {
        List<Quote> quotes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            SQL = "SELECT Quotes.id, Quotes.text, Quotes.score, Quotes.date, Users.name FROM Quotes JOIN Users ON (Quotes.user_id = Users.id)";
            ResultSet result = statement.executeQuery(SQL);
            while (result.next()) {
                Quote quote = new Quote();
                quote.setId(result.getInt("id"));
                quote.setText(result.getString("text"));
                quote.setScore(result.getInt("score"));
                quote.setDate(result.getDate("date"));
                quote.setAuthor(result.getString("name"));
                quotes.add(quote);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quotes;
    }

    public Quote getQuote(int id) {
        Quote quote = new Quote();
        try {
            Statement statement = connection.createStatement();
            SQL = "SELECT Quotes.id, Quotes.text, Quotes.user_id, Quotes.score, Quotes.date, Users.name FROM Quotes " +
                    "JOIN Users ON(Quotes.user_id = Users.id) WHERE Quotes.id = " + id;
            ResultSet result = statement.executeQuery(SQL);
            quote.setId(result.getInt("id"));
            quote.setText(result.getString("text"));
            quote.setUserId(result.getInt("user_id"));
            quote.setScore(result.getInt("score"));
            quote.setDate(result.getDate("date"));
            quote.setAuthor(result.getString("name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return quote;
//        return getQuotes().stream().filter(quote -> quote.getId() == id).findAny().orElse(null);
    }

    public void saveQuote(Quote quote) {
        List<User> users = getUsers();
        boolean flag = false;
        for (User user : users) {
            if (user.getId() == quote.getUserId()) {
                flag = true;
                break;
            }
            if (flag) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Quotes(text, user_id, score) SELECT ?, ?, ?" +
                            " WHERE EXISTS(select id from Users where Users.id = " + quote.getUserId());
                    preparedStatement.setString(1, quote.getText());
                    preparedStatement.setInt(2, quote.getUserId());
                    preparedStatement.setInt(3, quote.getScore());
//                    preparedStatement.setDate(4, quote.getDate());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users(id, name) VALUES(?, ?)");
                    preparedStatement.setInt(1, quote.getUserId());
                    preparedStatement.setString(2, quote.getAuthor());
                    preparedStatement.executeUpdate();
                    preparedStatement = connection.prepareStatement("INSERT INTO Quotes(text, user_id, score, date) SELECT ?, ?, ?");
                    preparedStatement.setString(1, quote.getText());
                    preparedStatement.setInt(2, quote.getUserId());
                    preparedStatement.setInt(3, quote.getScore());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void updateQuote(int id, Quote quote) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Quotes SET text = ?, score = ?, date = ? WHERE id = ?");
            preparedStatement.setString(1, quote.getText());
            preparedStatement.setInt(2, quote.getScore());
            preparedStatement.setDate(3, quote.getDate());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRandomQuote() {
        Random rand = new Random();
        Quote randomQuote = getQuotes().get(rand.nextInt(getQuotes().size()));
        return randomQuote.getText();
    }

    public void deleteQuote(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Quotes WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
