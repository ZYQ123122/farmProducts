package com.ruoyi.system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.ExpertConsultation;
import com.ruoyi.system.mapper.ExpertConsultationMapper;
import com.ruoyi.system.service.impl.ExpertConsultationServiceImpl;

@ExtendWith(MockitoExtension.class)
class ExpertConsultationServiceImplTest
{
    @Mock
    private ExpertConsultationMapper expertConsultationMapper;

    @InjectMocks
    private ExpertConsultationServiceImpl expertConsultationService;

    private ExpertConsultation consultation;

    @BeforeEach
    void setUp()
    {
        consultation = new ExpertConsultation();
        consultation.setId(1L);
        consultation.setUserId(10L);
        consultation.setExpertId(20L);
        consultation.setStatus("pending");
    }

    @Test
    void selectConsultationListByUserId_shouldReturnMapperResult()
    {
        List<ExpertConsultation> expected = Collections.singletonList(consultation);
        when(expertConsultationMapper.selectConsultationListByUserId(10L, "pending")).thenReturn(expected);

        assertSame(expected, expertConsultationService.selectConsultationListByUserId(10L, "pending"));
        verify(expertConsultationMapper).selectConsultationListByUserId(10L, "pending");
    }

    @Test
    void selectConsultationListByUserId_shouldRejectInvalidStatus()
    {
        assertThrows(ServiceException.class,
            () -> expertConsultationService.selectConsultationListByUserId(10L, "invalid"));
        verifyNoInteractions(expertConsultationMapper);
    }

    @Test
    void selectExpertConsultationById_shouldReturnNullWhenIdMissing()
    {
        assertNull(expertConsultationService.selectExpertConsultationById(null));
        verifyNoInteractions(expertConsultationMapper);
    }

    @Test
    void insertExpertConsultation_shouldAutoFillDefaults()
    {
        ExpertConsultation toInsert = new ExpertConsultation();
        toInsert.setUserId(10L);
        toInsert.setExpertId(22L);

        when(expertConsultationMapper.insertExpertConsultation(toInsert)).thenReturn(1);

        assertEquals(1, expertConsultationService.insertExpertConsultation(toInsert));
        assertEquals("pending", toInsert.getStatus());
        assertEquals(Integer.valueOf(0), toInsert.getIsDeleted());
        verify(expertConsultationMapper).insertExpertConsultation(toInsert);
    }

    @Test
    void insertExpertConsultation_shouldValidateRequiredFields()
    {
        ExpertConsultation toInsert = new ExpertConsultation();
        toInsert.setExpertId(20L);

        assertThrows(IllegalArgumentException.class,
            () -> expertConsultationService.insertExpertConsultation(toInsert));
        verifyNoInteractions(expertConsultationMapper);
    }

    @Test
    void updateExpertConsultation_shouldValidateStatus()
    {
        ExpertConsultation toUpdate = new ExpertConsultation();
        toUpdate.setId(1L);
        toUpdate.setStatus("invalid");

        assertThrows(ServiceException.class,
            () -> expertConsultationService.updateExpertConsultation(toUpdate));
        verify(expertConsultationMapper, never()).updateExpertConsultation(toUpdate);
    }

    @Test
    void replyConsultation_shouldSucceedWhenExpertMatches()
    {
        when(expertConsultationMapper.selectExpertConsultationById(1L)).thenReturn(consultation);
        when(expertConsultationMapper.replyConsultation(1L, 20L, "answer")).thenReturn(1);

        assertEquals(1, expertConsultationService.replyConsultation(1L, 20L, "answer"));
        verify(expertConsultationMapper).replyConsultation(1L, 20L, "answer");
    }

    @Test
    void replyConsultation_shouldFailWhenRecordMissing()
    {
        when(expertConsultationMapper.selectExpertConsultationById(1L)).thenReturn(null);

        assertThrows(ServiceException.class,
            () -> expertConsultationService.replyConsultation(1L, 20L, "answer"));
        verify(expertConsultationMapper, never()).replyConsultation(1L, 20L, "answer");
    }

    @Test
    void replyConsultation_shouldFailWhenExpertMismatch()
    {
        ExpertConsultation otherExpert = new ExpertConsultation();
        otherExpert.setExpertId(30L);
        otherExpert.setStatus("pending");

        when(expertConsultationMapper.selectExpertConsultationById(1L)).thenReturn(otherExpert);

        assertThrows(ServiceException.class,
            () -> expertConsultationService.replyConsultation(1L, 20L, "answer"));
    }

    @Test
    void replyConsultation_shouldFailWhenClosed()
    {
        consultation.setStatus("closed");
        when(expertConsultationMapper.selectExpertConsultationById(1L)).thenReturn(consultation);

        assertThrows(ServiceException.class,
            () -> expertConsultationService.replyConsultation(1L, 20L, "answer"));
        verify(expertConsultationMapper, never()).replyConsultation(1L, 20L, "answer");
    }

    @Test
    void softDeleteExpertConsultation_shouldValidateParams()
    {
        assertThrows(IllegalArgumentException.class,
            () -> expertConsultationService.softDeleteExpertConsultation(1L, null));
        verifyNoInteractions(expertConsultationMapper);
    }

    @Test
    void changeConsultationStatus_shouldRejectInvalidStatus()
    {
        assertThrows(ServiceException.class,
            () -> expertConsultationService.changeConsultationStatus(1L, "invalid"));
        verifyNoInteractions(expertConsultationMapper);
    }
}

