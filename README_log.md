# Development Log

## 1. Environment and Version Information

- **OS**: Linux (Container)
- **Java**: OpenJDK 17
- **Maven**: 3.8.7
- **Node.js**: 20.x
- **Database**: H2 (File-based)
- **Backend**: Spring Boot 3.2.0
- **Frontend**: Vue 3 + Element Plus + Vite

## 2. Startup and Deployment

### Startup Script
The project includes a `start.sh` script in the root directory.
To start the application:
```bash
./start.sh
```
This script will:
1. Check and install Java/Maven if missing.
2. Start the Backend (Spring Boot) on port 8080.
3. Install Frontend dependencies.
4. Start the Frontend (Vite) on port 5173.

### Manual Startup
**Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```

## 3. Implementation Details

### Backend
- **Framework**: Spring Boot
- **Security**: Spring Security + JWT
- **Database**: H2
- **Entities**: User, Teacher, Student, Course, CourseClass, Enrollment, Grade
- **API**: RESTful API for Admin, Teacher, Student

### Frontend
- **Framework**: Vue 3
- **UI Library**: Element Plus
- **State Management**: Pinia
- **Routing**: Vue Router

### Data Seeding
- The application automatically seeds data from `/workspace/data/021/seed/` on startup if the database is empty.
- Seed data includes Users, Teachers, Students, Courses, Classes.

## 4. Testing

### Smoke Test
The smoke test script is located at `/workspace/data/021/tests/smoke_test.sh`.
To run it:
```bash
/workspace/data/021/tests/smoke_test.sh
```

### Manual Verification
- **Login**: Supports Admin, Teacher, Student login.
- **Admin**: Can manage Teachers, Students, Courses, Classes.
- **Teacher**: Can view classes, students, and enter grades.
- **Student**: Can view available classes, enroll, drop, view schedule, view grades.

## 5. Known Issues and Future Improvements
- **Role Management**: Currently roles are simple strings/enums. RBAC could be improved.
- **Validation**: Basic validation is implemented. More comprehensive validation is needed.
- **Error Handling**: Basic error handling. Could be improved with global exception handler.
- **Testing**: Unit tests and Integration tests are not fully implemented.

## 6. Screenshots
(Screenshots would be placed here in a real environment)
