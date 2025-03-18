# Blog Application

## Overview
This is a full-stack blog application built with **Spring Boot** for the backend and **React.js** for the frontend. The application utilizes **JWT authentication** and includes three user roles: **User, Publisher, and Admin**.

## Tech Stack
- **Backend:** Spring Boot, PostgreSQL, JWT Authentication
- **Frontend:** React.js
- **Database:** PostgreSQL

## Database Schema
The application contains four main tables:
1. **User Table**: Stores user details
   - `id`
   - `email`
   - `username`
   - `password`
2. **Blog Table**: Stores blog details
   - `id`
   - `blog_url`
   - `description`
   - `title`
   - `publisher_id`
3. **Like Table**: Stores likes for blogs
4. **Comment Table**: Stores comments on blogs

## Features
### **Admin Dashboard**
- Create Publisher
- Edit Publisher
- Delete Publisher
- Get all Publishers

### **Publisher Dashboard**
- Create Blog
- Delete Blog
- Get all Blogs

### **Public Blog View**
- All users can view published blogs via a public URL.
- If you are not logged in, you cannot like blogs.
- To like or comment on a blog, you must log in with a **User** role.

## Authentication & Authorization
- JWT authentication is implemented to secure API endpoints.
- Access control is managed based on roles (**Admin, Publisher, User**).

