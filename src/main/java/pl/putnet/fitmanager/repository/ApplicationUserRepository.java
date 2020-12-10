package pl.putnet.fitmanager.repository;

import org.springframework.data.repository.CrudRepository;

import pl.putnet.fitmanager.model.ApplicationUser;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Integer> {
	ApplicationUser findByEmail(String email);
}
