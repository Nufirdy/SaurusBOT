package ent.otego.saurus.core.testimpl;

import ent.otego.saurus.core.Fixtures;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.CustomLeaderboardRepository;
import java.util.*;
import java.util.function.Function;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public class CustomLeaderboardRepositoryTestImpl implements CustomLeaderboardRepository {

    private final List<CustomLeaderboard> customLeaderboards;

    public CustomLeaderboardRepositoryTestImpl() {
        customLeaderboards = new ArrayList<>();
        customLeaderboards.add(Fixtures.LEADERBOARD_A);
        customLeaderboards.add(Fixtures.LEADERBOARD_B);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends CustomLeaderboard> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<CustomLeaderboard> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CustomLeaderboard getOne(UUID uuid) {
        return null;
    }

    @Override
    public CustomLeaderboard getById(UUID uuid) {
        return null;
    }

    @Override
    public CustomLeaderboard getReferenceById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CustomLeaderboard> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CustomLeaderboard> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CustomLeaderboard, R> R findBy(
            Example<S> example,
            Function<FetchableFluentQuery<S>, R> queryFunction
    ) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> S save(S entity) {
        return null;
    }

    @Override
    public <S extends CustomLeaderboard> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CustomLeaderboard> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public List<CustomLeaderboard> findAll() {
        return customLeaderboards;
    }

    @Override
    public List<CustomLeaderboard> findAllById(Iterable<UUID> uuids) {
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
    public void delete(CustomLeaderboard entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends CustomLeaderboard> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<CustomLeaderboard> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CustomLeaderboard> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public CustomLeaderboard findBySubscriberId(long chatId) {
        return null;
    }

    @Override
    public Set<MapInfo> findAllTrackedMapsForPlayer(PlayerAccount playerAccount) {
        return null;
    }
}
