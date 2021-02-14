package com.infinity.i_attendance.ui.setting.role;

public class Role {
    private int id;
    private String role_name;
    private int access_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public int getAccess_code() {
        return access_code;
    }

    public void setAccess_code(int access_code) {
        this.access_code = access_code;
    }

    @Override
    public String toString() {
        return role_name;
    }

    public enum Setting {

        SUPER_ADMIN(111111, "Super Admin"),
        GENERAL(999999, "General"),
        APP_SETUP(1, "App Setup"),
        MANAGE_USER(2, "Manage user (Add, Delete, Edit profile)"),
        MANAGE_ATTENDANCE(3, "Manage attendance (Edit attendance)"),
        MANAGE_LEAVE(4, "Manege leave (Accept, Decline leave request)");

        private final int access_code;
        private final String displayName;

        Setting(int access_code, String displayName) {
            this.access_code = access_code;
            this.displayName = displayName;
        }

        // Getters
        public String getDisplayName() {
            return this.displayName;
        }

        public int getAccess_code() {
            return this.access_code;
        }

        @Override
        public String toString() {
            return "Setting{" +
                    "access_code=" + access_code +
                    ", displayName='" + displayName + '\'' +
                    '}';
        }
    }

   /* @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role_name='" + role_name + '\'' +
                ", access_code=" + access_code +
                '}';
    }*/
}
