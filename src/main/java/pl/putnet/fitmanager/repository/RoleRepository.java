package pl.putnet.fitmanager.repository;

import org.springframework.data.repository.CrudRepository;

import pl.putnet.fitmanager.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	Role findByName(String name);
	Role findByValue(Integer value);

}
