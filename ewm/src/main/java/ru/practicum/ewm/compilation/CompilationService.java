package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.error.ResourceNotFoundException;
import ru.practicum.ewm.event.EventEntity;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.participation.ParticipationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final CriteriaCompilationRepository criteriaCompilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<EventEntity> eventEntityList = new ArrayList<>();
        List<Long> eventIdList = newCompilationDto.getEvents();

        if (eventIdList != null) {
            eventEntityList = eventRepository.findByEventIdList(eventIdList);
        }

        CompilationEntity compilationEntity = compilationMapper.buildEntity(newCompilationDto, eventEntityList);
        Map<Long, Long> confirmed = participationRepository.eventId_vs_ConfirmedParticipants(eventIdList);

        List<EventShortDto> eventShortDtoList = eventEntityList.stream()
                .map(eventEntity -> {
                    Integer confirmedReq = confirmed.getOrDefault(eventEntity.getId(), 0L).intValue();
                    return eventMapper.buildEventShortDto(eventEntity, confirmedReq);
                })
                .collect(Collectors.toList());

        compilationRepository.save(compilationEntity);
        return compilationMapper.buildDto(compilationEntity, eventShortDtoList);
    }

    public void delCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new ResourceNotFoundException(String.format("Compilation with id: %s not found", compId));
        }
        compilationRepository.deleteById(compId);
    }

    public CompilationDto PatchCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        CompilationEntity compilationEntity = compilationRepository.findById(compId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Compilation with id: %s not found", compId)));

        List<EventEntity> eventEntityList = new ArrayList<>();
        List<Long> eventIdList = updateCompilationRequest.getEvents();
        if (eventIdList != null) {
            eventEntityList = eventRepository.findByEventIdList(eventIdList);
        }

        CompilationEntity updatedCompilationEntity =
                compilationMapper.patchEntity(updateCompilationRequest, compilationEntity, eventEntityList);
        Map<Long, Long> confirmed = participationRepository.eventId_vs_ConfirmedParticipants(eventIdList);

        List<EventShortDto> eventShortDtoList = eventEntityList.stream()
                .map(eventEntity -> {
                    Integer confirmedReq = confirmed.getOrDefault(eventEntity.getId(), 0L).intValue();
                    return eventMapper.buildEventShortDto(eventEntity, confirmedReq);
                })
                .collect(Collectors.toList());

        compilationRepository.save(updatedCompilationEntity);
        return compilationMapper.buildDto(updatedCompilationEntity, eventShortDtoList);
    }

    public CompilationDto findById(Long compId) {
        CompilationEntity compilationEntity = compilationRepository.findById(compId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Compilation with id: %s not found", compId)));

        List<Long> eventIdList = compilationEntity.getEvents().stream()
                .map(EventEntity::getId)
                .collect(Collectors.toList());

        List<EventEntity> eventEntityList = eventRepository.findByEventIdList(eventIdList);
        Map<Long, Long> confirmed = participationRepository.eventId_vs_ConfirmedParticipants(eventIdList);

        List<EventShortDto> eventShortDtoList = eventEntityList.stream()
                .map(eventEntity -> {
                    Integer confirmedReq = confirmed.getOrDefault(eventEntity.getId(), 0L).intValue();
                    return eventMapper.buildEventShortDto(eventEntity, confirmedReq);
                })
                .collect(Collectors.toList());

        compilationRepository.save(compilationEntity);
        return compilationMapper.buildDto(compilationEntity, eventShortDtoList);
    }

    public List<CompilationDto> findWithParams(Boolean pinned, Integer from, Integer size) {
        Page<CompilationEntity> compilationEntityPage = criteriaCompilationRepository.findWithParams(pinned, from, size);

        List<CompilationDto> compilationDtoList = new ArrayList<>();

        for (CompilationEntity compilationEntity : compilationEntityPage.getContent()) {
            List<Long> eventIdList = compilationEntity.getEvents().stream()
                    .map(EventEntity::getId)
                    .collect(Collectors.toList());

            List<EventEntity> eventEntityList = eventRepository.findByEventIdList(eventIdList);
            Map<Long, Long> confirmed = participationRepository.eventId_vs_ConfirmedParticipants(eventIdList);

            List<EventShortDto> eventShortDtoList = eventEntityList.stream()
                    .map(eventEntity -> {
                        Integer confirmedReq = confirmed.getOrDefault(eventEntity.getId(), 0L).intValue();
                        return eventMapper.buildEventShortDto(eventEntity, confirmedReq);
                    })
                    .collect(Collectors.toList());

            CompilationDto compilationDto = compilationMapper.buildDto(compilationEntity, eventShortDtoList);
            compilationDtoList.add(compilationDto);
        }
        return compilationDtoList;
    }
}
