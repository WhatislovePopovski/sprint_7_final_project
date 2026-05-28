package models;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier() {
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static CourierBuilder builder() {
        return new CourierBuilder();
    }

    public static class CourierBuilder {
        private String login;
        private String password;
        private String firstName;

        public CourierBuilder login(String login) {
            this.login = login;
            return this;
        }

        public CourierBuilder password(String password) {
            this.password = password;
            return this;
        }

        public CourierBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Courier build() {
            return new Courier(login, password, firstName);
        }
    }
}