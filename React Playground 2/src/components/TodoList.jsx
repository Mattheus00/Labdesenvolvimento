import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrash } from '@fortawesome/free-solid-svg-icons';

export const TodoList = ({ task }) => {
  // Verificação para garantir que task não é undefined
  if (!task) {
    return null; // ou qualquer fallback apropriado
  }

  return (
    <div className='Todo'>
      <p className={`${task.completed ? "completed" : "incompleted"}`}>
        {task.description}
      </p>
    </div>
  );
}
