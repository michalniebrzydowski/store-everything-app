package pl.edu.pb.storeeverything.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pb.storeeverything.entity.Role;
import pl.edu.pb.storeeverything.repository.RoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
