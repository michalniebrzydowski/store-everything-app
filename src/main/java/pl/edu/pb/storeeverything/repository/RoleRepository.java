package pl.edu.pb.storeeverything.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pb.storeeverything.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);
}
