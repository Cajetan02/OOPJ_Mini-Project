    package com.sportsmanager.model;

    import javafx.beans.property.*;

    /**
     * User model for authentication and role-based access
     */
    public class User {
        private final StringProperty id;
        private final StringProperty username;
        private final StringProperty fullName;
        private final StringProperty email;
        private final StringProperty role; // admin, manager, player, viewer
        private final StringProperty avatarUrl;
        private final StringProperty phone;
        private final BooleanProperty isActive;

        public User(String id, String username, String fullName, String email, String role) {
            this.id = new SimpleStringProperty(id);
            this.username = new SimpleStringProperty(username);
            this.fullName = new SimpleStringProperty(fullName);
            this.email = new SimpleStringProperty(email);
            this.role = new SimpleStringProperty(role);
            this.avatarUrl = new SimpleStringProperty("");
            this.phone = new SimpleStringProperty("");
            this.isActive = new SimpleBooleanProperty(true);
        }

        // ID
        public String getId() { return id.get(); }
        public void setId(String value) { id.set(value); }
        public StringProperty idProperty() { return id; }

        // Username
        public String getUsername() { return username.get(); }
        public void setUsername(String value) { username.set(value); }
        public StringProperty usernameProperty() { return username; }

        // Full Name
        public String getFullName() { return fullName.get(); }
        public void setFullName(String value) { fullName.set(value); }
        public StringProperty fullNameProperty() { return fullName; }

        // Email
        public String getEmail() { return email.get(); }
        public void setEmail(String value) { email.set(value); }
        public StringProperty emailProperty() { return email; }

        // Role
        public String getRole() { return role.get(); }
        public void setRole(String value) { role.set(value); }
        public StringProperty roleProperty() { return role; }

        // Avatar URL
        public String getAvatarUrl() { return avatarUrl.get(); }
        public void setAvatarUrl(String value) { avatarUrl.set(value); }
        public StringProperty avatarUrlProperty() { return avatarUrl; }

        // Phone
        public String getPhone() { return phone.get(); }
        public void setPhone(String value) { phone.set(value); }
        public StringProperty phoneProperty() { return phone; }

        // Is Active
        public boolean isActive() { return isActive.get(); }
        public void setActive(boolean value) { isActive.set(value); }
        public BooleanProperty isActiveProperty() { return isActive; }

        @Override
        public String toString() {
            return fullName.get() + " (" + username.get() + ")";
        }
    }