package vn.edu.actvn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.actvn.server.entity.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {

    Optional<Attendance> findByEntityClass_ClassIdAndDate(String classId, LocalDate date);

    @Query("""
    SELECT DISTINCT a FROM Attendance a
        LEFT JOIN a.studentAttendances sa
    WHERE (:studentId IS NULL OR LOWER(sa.studentId) LIKE LOWER(CONCAT('%', CAST(:studentId AS string), '%')))
      AND (:classId IS NULL OR LOWER(a.entityClass.classId) LIKE LOWER(CONCAT('%', CAST(:classId AS string), '%')))
      AND (a.date = COALESCE(:date , a.date))
""")
    Page<Attendance> search(
            @Param("studentId") String studentId,
            @Param("classId") String classId,
            @Param("date") LocalDate date,
            Pageable pageable);

    List<Attendance> findByEntityClass_ClassIdAndDateBetween(String classId, LocalDate start, LocalDate end);
//    @Query("""
//    SELECT a
//    FROM Attendance a
//    WHERE a.entityClass.classId = :classId
//      AND a.date BETWEEN :start AND :end
//    """)
//    List<Attendance> findAttendances(String classId, LocalDate start, LocalDate end);

    long countByEntityClass_ClassIdAndStudentAttendances_StudentIdAndStudentAttendances_Status(String classId, String studentId, Attendance.Status status);
//    @Query("""
//    SELECT COUNT(a)
//    FROM Attendance a
//    JOIN a.studentAttendances sa
//    WHERE a.entityClass.classId = :classId
//      AND sa.studentId = :studentId
//      AND sa.status = :status
//    """)
//    long countStudentStatus(String classId, String studentId, Attendance.Status status);
//

}
