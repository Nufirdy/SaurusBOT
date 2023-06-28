package ent.otego.saurus.core.testimpl;

import ent.otego.saurus.core.Fixtures;
import ent.otego.saurus.core.model.*;
import ent.otego.saurus.core.repository.MapRecordRepository;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

public class MapRecordRepositoryTestImpl implements MapRecordRepository {

    private final Set<MapRecord> mapRecords;

    public MapRecordRepositoryTestImpl() {
        mapRecords = new HashSet<>();
        mapRecords.add(Fixtures.MAP_RECORD_1_PLAYER_A_ON_A);
        mapRecords.add(Fixtures.MAP_RECORD_2_PLAYER_A_ON_A);
        mapRecords.add(Fixtures.MAP_RECORD_1_PLAYER_B_ON_A);
        mapRecords.add(Fixtures.MAP_RECORD_2_PLAYER_B_ON_A);
        mapRecords.add(Fixtures.MAP_RECORD_1_PLAYER_B_ON_B);
        mapRecords.add(Fixtures.MAP_RECORD_1_PLAYER_C_ON_B);
    }

    @Override
    public MapRecord findFastestByMap(MapInfo mapInfo) {
        return null;
    }

    @Override
    public Optional<MapRecord> findFastestRecordByPlayerOnMap(MapInfo mapInfo, PlayerAccount playerAccount) {
        return null;
    }

    @Override
    public List<MapRecord> getCustomLeaderboardRecords(Collection<UUID> accounts, UUID mapInfo) {
        return mapRecords.stream()
                .filter(mapRecord -> accounts.contains(mapRecord.getAccount().getId()))
                .filter(mapRecord -> mapRecord.getMapInfo().getId().equals(mapInfo))
                .collect(Collectors.groupingBy(MapRecord::getMapInfo,
                        Collectors.groupingBy(MapRecord::getAccount)))
                .entrySet().stream()
                .flatMap(entry -> {
                    return entry.getValue().entrySet().stream()
                            .map(innerEntry -> innerEntry.getValue().stream()
                                    .min(Comparator.comparing(MapRecord::getTime))
                                    .get());
                })
                .toList();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends MapRecord> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends MapRecord> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<MapRecord> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public MapRecord getOne(UUID uuid) {
        return null;
    }

    @Override
    public MapRecord getById(UUID uuid) {
        return null;
    }

    @Override
    public MapRecord getReferenceById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends MapRecord> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends MapRecord> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends MapRecord> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends MapRecord> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends MapRecord> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends MapRecord> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends MapRecord, R> R findBy(
            Example<S> example,
            Function<FetchableFluentQuery<S>, R> queryFunction
    ) {
        return null;
    }

    @Override
    public <S extends MapRecord> S save(S entity) {
        mapRecords.add(entity);
        return entity;
    }

    @Override
    public <S extends MapRecord> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<MapRecord> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
    }

    @Override
    public List<MapRecord> findAll() {
        return null;
    }

    @Override
    public List<MapRecord> findAllById(Iterable<UUID> uuids) {
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
    public void delete(MapRecord entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }

    @Override
    public void deleteAll(Iterable<? extends MapRecord> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<MapRecord> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<MapRecord> findAll(Pageable pageable) {
        return null;
    }
}
