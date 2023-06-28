package ent.otego.saurus.core.testimpl;

import ent.otego.saurus.core.Fixtures;
import ent.otego.saurus.core.model.PlayerAccount;
import java.util.*;
import java.util.function.Function;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public class PlayerAccountRepositoryTestImpl implements
        ent.otego.saurus.core.repository.PlayerAccountRepository {

    private final Set<PlayerAccount> playerAccounts;

    public PlayerAccountRepositoryTestImpl() {
        playerAccounts = new HashSet<>();
        playerAccounts.add(Fixtures.PLAYER_A);
        playerAccounts.add(Fixtures.PLAYER_B);
        playerAccounts.add(Fixtures.PLAYER_C);
        playerAccounts.add(Fixtures.PLAYER_D);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PlayerAccount> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends PlayerAccount> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<PlayerAccount> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PlayerAccount getOne(UUID uuid) {
        return null;
    }

    @Override
    public PlayerAccount getById(UUID uuid) {
        return null;
    }

    @Override
    public PlayerAccount getReferenceById(UUID uuid) {
        return playerAccounts.stream()
                .filter(playerAccount -> playerAccount.getId().equals(uuid))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public <S extends PlayerAccount> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PlayerAccount> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PlayerAccount> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PlayerAccount> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PlayerAccount> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PlayerAccount> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends PlayerAccount, R> R findBy(
            Example<S> example,
            Function<FetchableFluentQuery<S>, R> queryFunction
    ) {
        return null;
    }

    @Override
    public <S extends PlayerAccount> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PlayerAccount> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PlayerAccount> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public List<PlayerAccount> findAll() {
        return null;
    }

    @Override
    public List<PlayerAccount> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public void delete(PlayerAccount entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends PlayerAccount> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<PlayerAccount> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PlayerAccount> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Set<PlayerAccount> findAllTrackedPlayers() {
        return null;
    }
}
