package ru.practicum.ewm.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventEntity;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationMapper {
    public CompilationEntity buildEntity(NewCompilationDto newCompilationDto, List<EventEntity> eventEntityList) {
        return CompilationEntity.builder()
                .events(eventEntityList)
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }

    public CompilationDto buildDto(CompilationEntity compilationEntity, List<EventShortDto> eventShortDtoList) {
        return CompilationDto.builder()
                .events(eventShortDtoList)
                .id(compilationEntity.getId())
                .pinned(compilationEntity.isPinned())
                .title(compilationEntity.getTitle())
                .build();
    }

    public CompilationEntity patchEntity(UpdateCompilationRequest updateCompilationRequest,
                                         CompilationEntity compilationEntity,
                                         List<EventEntity> eventEntityList) {

        return CompilationEntity.builder()
                .events(eventEntityList != null ? eventEntityList : compilationEntity.getEvents())
                .id(compilationEntity.getId())
                .title(updateCompilationRequest.getTitle() != null ? updateCompilationRequest.getTitle() : compilationEntity.getTitle())
                .pinned(updateCompilationRequest.getPinned() != null ? updateCompilationRequest.getPinned() : compilationEntity.isPinned())
                .build();
    }
}
